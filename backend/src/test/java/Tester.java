import com.veeva.vault.handler.IntegrationHandler;
import com.veeva.vault.model.*;
import com.veeva.vault.vapil.api.model.VaultModel;
public class Tester {

	public static void main(String[] args) {

		String vaultDNS = "verteobiotech.veevavault.com";
		String sessionId = "CCF16FD82F718FA276DCE408FB698745B04DD36BCDE828C3AE3C063EC6F745AA55601AB414A3DC9F5F44DF422DE24D33290A3EE7A252A986EE62C5475F5EB31A";

		testGetKanban(vaultDNS, sessionId);
		//testSetKanban(vaultDNS, sessionId);
	}

	public static IntegrationResponse testGetKanban(String vaultDNS, String sessionId) {
		IntegrationRequestBodyParams bodyParams = new IntegrationRequestBodyParams();
		bodyParams.setVaultDNS(vaultDNS);
		bodyParams.setSessionId(sessionId);
		bodyParams.setActionType(IntegrationRequestBodyParams.ActionType.GET_KANBAN_DATA);
		bodyParams.setBoard("access_request_security_profiles_board__c");
		bodyParams.setPage(1);

		IntegrationRequest request = new IntegrationRequest();
		request.setBodyParams(bodyParams);
		IntegrationHandler integrationHandler = new IntegrationHandler(request);
		IntegrationResponse response = integrationHandler.process();
		System.out.println(response);

		return response;
	}

	public static void testSetKanban(String vaultDNS, String sessionId) {

		IntegrationResponse getResponse = testGetKanban(vaultDNS, sessionId);
		if (getResponse != null) {
			KanbanBoard kanbanBoard = (KanbanBoard)getResponse.getData();

			boolean foundOne = false;
			for (KanbanSwimlane swimlane : kanbanBoard.getSwimlanes()) {
				for (KanbanCard card : swimlane.getCards()) {
					if (!foundOne) {
						VaultModel newValues = new VaultModel();
						newValues.set("id", card.getOldValues().get("id"));
						newValues.set("comment__c", "Testers!");
						card.setNewValues(newValues);
						foundOne = true;
					}
				}
			}

			IntegrationRequestBodyParams bodyParams = new IntegrationRequestBodyParams();
			bodyParams.setVaultDNS(vaultDNS);
			bodyParams.setSessionId(sessionId);
			bodyParams.setBoard("access_request_priority_board__c");
			bodyParams.setAction(IntegrationRequestBodyParams.ActionType.SET_KANBAN_DATA.getValue());
			bodyParams.setData(kanbanBoard);

			IntegrationRequest request = new IntegrationRequest();
			request.setBodyParams(bodyParams);
			IntegrationHandler integrationHandler = new IntegrationHandler(request);
			IntegrationResponse response = integrationHandler.process();
			System.out.println(response);

		}

	}
}
