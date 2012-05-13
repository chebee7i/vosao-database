/**
 * Database plugin for Vosao CMS.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: pquiring@gmail.com
 */

package org.vosao.plugins.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.dom4j.DocumentException;
import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.utils.StreamUtil;
import org.vosao.velocity.plugin.AbstractVelocityPlugin;

import com.google.appengine.api.datastore.*;
import static com.google.appengine.api.datastore.FetchOptions.Builder.*;

/**
 * 
 * @author Peter Quiring
 *
 */
public class DatabaseVelocityPlugin extends AbstractVelocityPlugin {

	private DatabaseEntryPoint entryPoint;
	private DatastoreService ds;
	
	public DatabaseVelocityPlugin(DatabaseEntryPoint enrtyPoint, Business aBusiness) {
		this.entryPoint = enrtyPoint;
		setBusiness(aBusiness);
		ds = aBusiness.getSystemService().getDatastore();
		if (ds == null) {
		  ds = DatastoreServiceFactory.getDatastoreService();
		}
	}
	
	public Query createQuery(String table) {
	  return new Query(table);
	}

	public Query addFilter(Query query, String col, String operator, Object val) {
	  Query.FilterOperator qfo = null;
	  if (operator.equals("==")) qfo = Query.FilterOperator.EQUAL;
	  else if (operator.equals(">")) qfo = Query.FilterOperator.GREATER_THAN;
	  else if (operator.equals(">=")) qfo = Query.FilterOperator.GREATER_THAN_OR_EQUAL;
	  else if (operator.equals("in")) qfo = Query.FilterOperator.IN;
	  else if (operator.equals("<")) qfo = Query.FilterOperator.LESS_THAN;
	  else if (operator.equals("<=")) qfo = Query.FilterOperator.LESS_THAN_OR_EQUAL;
	  else if (operator.equals("!=")) qfo = Query.FilterOperator.NOT_EQUAL;
	  if (qfo == null) return null;
	  query.addFilter(col, qfo, val);
	  return query;
	}

    public Query addSort(Query query, String col, String direction) {
      Query.SortDirection dir = null;
      if (direction.equals("ASCENDING")) dir = Query.SortDirection.ASCENDING;
      else if (direction.equals("DESCENDING")) dir = Query.SortDirection.DESCENDING;
      if (dir == null) return null;
      query.addSort(col, dir);
      return query;
    }	

	public long insert(String table, List<String> cols, List<Object> data) {
    Entity e = new Entity(table);
    for(int col=0;col<cols.size();col++) {
      e.setProperty(cols.get(col), data.get(col));
    }  
    return ds.put(e).getId();
	}

	public List<Object> selectRow(List<String> cols, Query query) {
	  PreparedQuery pq = ds.prepare(query);
	  List<Entity> el = pq.asList(withDefaults());
	  if (el.size() < 1) return new ArrayList<Object>();
	  Entity e = el.get(0);
	  int colssize = cols.size();
	  ArrayList<Object> data = new ArrayList<Object>();
	  for(int col=0;col<colssize;col++) {
	    data.add(e.getProperty(cols.get(col)));
	  }
	  return data;
	}

	public List<List<Object>> selectRows(List<String> cols, Query query) {
	  return selectRows(cols, query, -1, -1);
	}

	public List<List<Object>> selectRows(List<String> cols, Query query, int offset, int limit) {
	  PreparedQuery pq = ds.prepare(query);
	  FetchOptions fo = withDefaults();
	  if (limit != -1) fo.limit(limit);
	  if (offset != -1) fo.offset(offset);
	  List<Entity> el = pq.asList(fo);
	  int elsize = el.size();
	  if (elsize < 1) return new ArrayList<List<Object>>();
	  int colssize = cols.size();
	  ArrayList<List<Object>> data = new ArrayList<List<Object>>();
	  for(int row=0;row<elsize;row++) {
  	  ArrayList<Object> dataRow = new ArrayList<Object>();
  	  Entity e = el.get(row);
  	  for(int col=0;col<colssize;col++) {
	      dataRow.add(e.getProperty(cols.get(col)));
	    }
  	  data.add(dataRow);
	  }  
	  return data;
	}
	
	public List<Long> selectKeys(Query query) {
	  PreparedQuery pq = ds.prepare(query.setKeysOnly());
	  List<Entity> el = pq.asList(withDefaults());
	  int elsize = el.size();
	  ArrayList<Long> keys = new ArrayList<Long>();
	  for(int row=0;row<elsize;row++) {
	    keys.add(new Long(el.get(row).getKey().getId()));
	  }
	  return keys;
	}

	public void delete(String table, long key) {
    ds.delete(KeyFactory.createKey(table, key));
	}

	public void delete(String table, List<Long> keys) {
	  int keyssize = keys.size();
	  for(int i=0;i<keyssize;i++) {
	    delete(table, keys.get(i));
	  }
	}
	
	public long update(String table, long key, List<String> cols, List<Object> data) {
    Entity e = new Entity(KeyFactory.createKey(table, key));
    int colssize = cols.size();
    for(int i=0;i<colssize;i++) {
      e.setProperty(cols.get(i), data.get(i));
    }  
    return ds.put(e).getId();
	}
}

