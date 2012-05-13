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

import org.apache.commons.lang.StringUtils;
import org.vosao.business.plugin.AbstractPluginEntryPoint;
import org.vosao.entity.PluginEntity;
import org.vosao.service.plugin.PluginServiceManager;

/**
 * 
 * @author Peter Quiring
 *
 */
public class DatabaseEntryPoint extends AbstractPluginEntryPoint {

	private DatabaseBackServiceManager backServiceManager;
	private DatabaseVelocityPlugin velocityPlugin;
	
	@Override
	public void init () {
		PluginEntity plugin = getDao().getPluginDao().getByName("database");
		if (StringUtils.isEmpty(plugin.getConfigData())) {
			plugin.setConfigData((new DatabaseConfig()).toXML());
			getDao().getPluginDao().save(plugin);	
		}
	}
	
	@Override
	public PluginServiceManager getPluginBackService() {
		if (backServiceManager == null) {
			backServiceManager = new DatabaseBackServiceManager(
					getBusiness()); 
		}
		return backServiceManager;
	}

	@Override
	public Object getPluginVelocityService() {
		if (velocityPlugin == null) {
			velocityPlugin = new DatabaseVelocityPlugin(this, getBusiness());
		}
		return velocityPlugin;
	}

	@Override
	public String getBundleName() {
		return "org.vosao.plugins.database.messages";
	}

}
