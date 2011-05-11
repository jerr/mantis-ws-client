package org.mantisbt.client.soap;
/**
 * MantisBT WS Client -Stub for MantisBT WebService.
 * MantisBT WS Client is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either
 * version 2 of the License, or (at your option) any later
 * version.
 * 
 * MantisBT SOAP Client is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MantisBT.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.math.BigInteger;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.mantisbt.client.IMantisWsService;
import org.mantisbt.client.MantisWsException;

/**
 * @author Jeremie Lagarde
 * @since 1.2.5
 */
public class MantisSoapService implements IMantisWsService<FilterData,IssueData> {

  private MantisConnectPortType mantisConnectPortType;
  private String username;
  private String password;
  
  public MantisSoapService(URL webServiceURL) {
    MantisConnectLocator mantisConnectLocator = createMantisConnectLocator();
    try {
      if (webServiceURL == null) {
        mantisConnectPortType = mantisConnectLocator.getMantisConnectPort();
      } else {
        mantisConnectPortType = mantisConnectLocator.getMantisConnectPort(webServiceURL);
      }
    } catch (ServiceException e) {
      throw new MantisWsException("MantisSoapService creation error", e);
    }
  }

  public void connect(String login, String password) {
    this.username = login;
    this.password = password;
    try {
      String version = mantisConnectPortType.mc_version();
    } catch (RemoteException e) {
      throw new MantisWsException("MantisSoapService.connect("+login+", ******) error", e);
    }
  }

  public FilterData[] getFilters(BigInteger porjectId) {
    try {
      return mantisConnectPortType.mc_filter_get(username, password, porjectId);
    } catch (RemoteException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public IssueData[] getIssues(FilterData filter) {
    List<IssueData> issues = new ArrayList<IssueData>();
    IssueData[] result = null;
    int page = 1;
    do {
      try {
        result = mantisConnectPortType.mc_filter_get_issues(username, password, filter.getProject_id(), filter.getId(), BigInteger.valueOf(page++),
            BigInteger.valueOf(100));
      } catch (RemoteException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      issues.addAll(Arrays.asList(result));
    } while (result.length == 100);
    return issues.toArray(new IssueData[issues.size()]);
  }

  public void disconnect() throws RemoteException {
  }

  protected MantisConnectLocator createMantisConnectLocator() {
    return new MantisConnectLocator();
  }

}
