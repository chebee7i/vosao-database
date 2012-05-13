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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 
 * @author Peter Quiring
 *
 */
public class DatabaseConfig {

	private static final Log logger = LogFactory.getLog(DatabaseConfig.class);

  //private config data (none)	

	public DatabaseConfig() {
	  //init config data
	}
	
	public static DatabaseConfig parse(String xml) throws DocumentException {
		DatabaseConfig config = new DatabaseConfig();
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		//load config from XML
		return config;
	}
	
	public String toXML() {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("config");
		return doc.asXML();
	}
	
  //config data setters/getters...
  	
}

