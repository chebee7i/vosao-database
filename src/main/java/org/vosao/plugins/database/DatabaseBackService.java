/**
 * Database plugin for Vosao CMS.
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

import java.util.Collections;
import java.util.Map;

import org.dom4j.DocumentException;
import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.plugin.AbstractServicePlugin;

/**
 * 
 * @author Peter Quiring
 *
 */
public class DatabaseBackService extends AbstractServicePlugin {

	public DatabaseBackService(Business business) {
		setBusiness(business);
	}
	
	public DatabaseConfig getConfig() throws DocumentException {
		PluginEntity plugin = getDao().getPluginDao().getByName("database");
		return DatabaseConfig.parse(plugin.getConfigData());
	}
}
