package app;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class NetbackupPolicyRequest {
	
	private static Logger logger = LoggerFactory.getLogger(NetbackupPolicyRequest.class);
	
	static String default_policy = "{\"data\":"
			+ "{\"type\":\"policy\",\"id\":\"policynametmp\",\"attributes\":"
			+ "{\"policy\":"
			+ "{\"policyName\":\"policynametmp\",\"policyType\":\"policytypetmp\",\"policyAttributes\":"
			+ "{"
			+ "\"active\": true,\"alternateClientHostname\": null,\"applicationConsistent\": true,\"applicationDiscovery\": false,\"applicationProtection\": [],"
			+ "\"archivedRedoLogOptions\": null,\"autoManagedLabel\": null,\"autoManagedType\": 0,\"backupFileNameFormatOverrides\":null,\"backupHost\": null,\"backupSelectionType\": \"Clients\","
			+ "\"backupSetIdentifier\": null,\"blockIncremental\": false,\"checkpointIntervalMinutes\": null,\"clientCompress\": false,\"clientEncrypt\": false,"
			+ "\"clientType\": \"Clients\",\"collectBareMetalRestoreInfo\": false,\"collectTrueImageRestoreInfo\": \"Off\",\"crossMountPoints\": false,"
			+ "\"dataClassification\": null,\"dataMoverMachine\": null,\"databaseBackupShareOptions\": null,\"databaseOptions\": null,\"datafileCopyTag\": null,"
			+ "\"disableClientSideDeduplication\": false,\"discoveryLifetime\": 28800,\"effectiveDateUTC\": \"2018-06-13T18:56:07Z\",\"exchangeDatabaseBackupSource\": \"Off\","
			+ "\"exchangeServerList\": [],\"followNFSMounts\": false,\"granularRecovery\": false,\"hypervServer\": null,\"jobLimit\": 2147483647,\"keyword\": null,\"maximumLimits\": null,"
			+ "\"mediaOwner\": \"*ANY*\",\"offhostBackup\": false,\"offlineDatabaseBackup\": null,\"optimizedBackup\": false,\"parallelStreams\": null,\"priority\": 0,"
			+ "\"readOnlyTablespaceOptions\": null,\"retainSnapshot\": false,\"secondarySnapshotMethodArgs\": null,\"skipOfflineDatafiles\": null,\"snapshotBackup\": false,"
			+ "\"snapshotMethod\": null,\"snapshotMethodArgs\": null,\"storage\": \"storagetmp\",\"storageIsSLP\": false,\"transactionLogOptions\": null,"
			+ "\"useAccelerator\": false,\"useMultipleDataStreams\": false,\"useReplicationDirector\": false,\"useVirtualMachine\": 0,\"volumePool\": \"NetBackup\""
			+ "},"
			+ "\"clients\":[],\"schedules\":[],\"backupSelections\":"
			+ "{\"selections\":[]}}}}}";
	
	static String default_client="{\"data\": {\"type\":\"client\",\"attributes\": {\"hardware\": \"Linux\",\"hostName\": \"hostnametmp\",\"OS\": \"RedHat2.6.32\"}}}";
	static String default_schedule="{\"data\": {\"type\": \"schedule\",\"id\": \"schedulenametmp\",\"attributes\":{\"acceleratorForcedRescan\": false,"
			+ "\"backupCopies\": {\"copies\": [{\"failStrategy\": null,\"mediaOwner\": null,\"retentionPeriod\": "
			+ "{\"value\": retentionnumtmp,\"unit\": \"retentionperiodtmp\"},\"storage\": null,\"volumePool\": null}],\"priority\": -1},"
			+ "\"backupType\": \"Full Backup\",\"excludeDates\": {\"lastDayOfMonth\": false,\"recurringDaysOfMonth\": [],\"recurringDaysOfWeek\": [],\"specificDates\": []},"
			+ "\"frequencySeconds\": frequencytmp,\"includeDates\": {\"lastDayOfMonth\": false,\"recurringDaysOfMonth\": [],\"recurringDaysOfWeek\": [],\"specificDates\": []},"
			+ "\"mediaMultiplexing\": 1,\"retriesAllowedAfterRunDay\": false,\"scheduleName\": \"schedulenametmp\",\"scheduleType\": \"Frequency\",\"snapshotOnly\": false,"
			+ "\"startWindow\": startwindowtmp,\"storageIsSLP\": false,\"syntheticBackup\": false}}}";
	
	static String default_backupselections="{\"data\":{\"type\":\"backupSelection\",\"attributes\":{\"selections\":[\"selectionstmp\"]}}}";
	
	static String replacepolicy(String policyname, String policytype, String storage)
	{
		return default_policy.replace("policynametmp", policyname).replace("policytypetmp", policytype).replace("storagetmp", storage);
	}
	
	static String replaceselections(String selection)
	{
		return default_backupselections.replace("selectionstmp", selection);
	}
	
	static String replaceclient(String hostname)
	{
		return default_client.replace("hostnametmp", hostname);
	}
	//
	static String replaceschedule(String schedulename, String retentionnum, String retentionperoid, String frequencytime, String weekday, String hour)
	{
		String times = null;
		String startwindow = null;
		int hourtmp = 0;
		int weeknum = 0;
		if (frequencytime.equals("hours"))
		{
			times = "3600";
			startwindow = "[{\"dayOfWeek\": 1,\"startSeconds\": 0,\"durationSeconds\": 86399},{\"dayOfWeek\": 2,\"startSeconds\": 0,\"durationSeconds\": 86399},"
					+ "{\"dayOfWeek\": 3,\"startSeconds\": 0,\"durationSeconds\": 86399},{\"dayOfWeek\": 4,\"startSeconds\": 0,\"durationSeconds\": 86399},"
					+ "{\"dayOfWeek\": 5,\"startSeconds\": 0,\"durationSeconds\": 86399},{\"dayOfWeek\": 6,\"startSeconds\": 0,\"durationSeconds\": 86399},"
					+ "{\"dayOfWeek\": 7,\"startSeconds\": 0,\"durationSeconds\": 86399}]";
		}else if(frequencytime.equals("days"))
		{
			times = "86400";
			hourtmp=Integer.parseInt(hour);
			startwindow = "[{\"dayOfWeek\": 1,\"startSeconds\": "+hourtmp*60*60+",\"durationSeconds\": 7200},{\"dayOfWeek\": 2,\"startSeconds\": "+hourtmp*60*60+",\"durationSeconds\": 7200},"
					+ "{\"dayOfWeek\": 3,\"startSeconds\": "+hourtmp*60*60+",\"durationSeconds\": 7200},{\"dayOfWeek\": 4,\"startSeconds\": "+hourtmp*60*60+",\"durationSeconds\": 7200},"
					+ "{\"dayOfWeek\": 5,\"startSeconds\": "+hourtmp*60*60+",\"durationSeconds\": 7200},{\"dayOfWeek\": 6,\"startSeconds\": "+hourtmp*60*60+",\"durationSeconds\": 7200},"
					+ "{\"dayOfWeek\": 7,\"startSeconds\": "+hourtmp*60*60+",\"durationSeconds\": 7200}]";
		}else if(frequencytime.equals("weeks"))
		{
			times = "86400";
			hourtmp=Integer.parseInt(hour);
			if (weekday.equals("Mon"))
			{
				weeknum = 2;
			}else if(weekday.equals("Tue"))
			{
				weeknum = 3;
			}else if(weekday.equals("Wen"))
			{
				weeknum = 4;
			}else if(weekday.equals("Thu"))
			{
				weeknum = 5;
			}else if(weekday.equals("Fri"))
			{
				weeknum = 6;
			}else if(weekday.equals("Sat"))
			{
				weeknum = 7;
			}else if(weekday.equals("Sun"))
			{
				weeknum = 1;
			}
			startwindow = "[";
			for (int i=1; i<=7; i++)
			{
				if (weeknum == i)
				{
					startwindow +="{\"dayOfWeek\": "+i+",\"startSeconds\": "+hourtmp*60*60+",\"durationSeconds\": 7200},";
				}else{
					startwindow +="{\"dayOfWeek\": "+i+",\"startSeconds\": 0,\"durationSeconds\": 0},";
				}
			}
			startwindow = startwindow.substring(0,startwindow.length() - 1);
			startwindow +="]";
		}

			
		return default_schedule.replace("schedulenametmp", schedulename).replace("retentionnumtmp", retentionnum).replace("retentionperiodtmp", retentionperoid).replace("frequencytmp", times).replace("startwindowtmp", startwindow);
	}
	
	static String NBULoginin(String baseurl)
	{
		String token = null;
		String url = baseurl + "/login";
		HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.netbackup+json;version=2.0");
        RemoteRestClient client = new RemoteRestClient();
        try
        {
        	token = client.postRequestnoverify("{\"domainType\":\"unixpwd\",\"domainName\":\"eccbackup03\",\"userName\":\"root\",\"password\":\"mko0,lp-\"}", url, headers);
        }catch(Exception e)
		{
        	logger.error("login error!");
			//System.out.println("login error!");
		}
        
        JSONObject jsObj = JSON.parseObject(token);
        return jsObj.getString("token").toString();
	}
	
	static void NBULoginout(String jwt)
	{
        HttpHeaders headers = new HttpHeaders();
        RemoteRestClient client = new RemoteRestClient();
        headers.set("Content-Type", "application/vnd.netbackup+json;version=2.0");
        headers.set("Authorization", jwt);
        headers.set("Accept", "application/vnd.netbackup+json;version=2.0");
        try
        {
        	client.postRequestnoverify(null, "https://10.96.93.74:1556/netbackup/logout", headers);
        }catch(Exception e)
		{
        	logger.error("logout error!");
		}
        
        return;
	}
	
	static String post_netbackup_create_policy(String policyname, String policytype, String storage, String baseurl, String jwt)
	{
		String result = null;
		String policy = replacepolicy(policyname, policytype, storage);
		String url = baseurl + "/config/policies/";
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/vnd.netbackup+json;version=2.0");
        headers.set("Authorization", jwt);
        headers.set("Accept", "application/vnd.netbackup+json;version=2.0");
        headers.set("X-NetBackup-Policy-Use-Generic-Schema", "true");
        RemoteRestClient client = new RemoteRestClient();
        try
        {
        	result = client.postRequestnoverify(policy, url, headers);
        }catch(Exception e)
		{
        	logger.error("netbackup create policy error!");
		}
        
        //System.out.println(result);
        
		return policyname;
	}
	
	static void put_netbackup_selections(String policyname, String selection, String baseurl, String jwt)
	{
		String result = null;
		String selections = replaceselections(selection);
		String url = baseurl + "/config/policies/" + policyname + "/backupselections";
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/vnd.netbackup+json;version=2.0");
        headers.set("Authorization", jwt);
        headers.set("Accept", "application/vnd.netbackup+json;version=2.0");
        RemoteRestClient client = new RemoteRestClient();
        try
        {
        	result = client.putRequestnoverify(selections, url, headers);
        }catch(Exception e)
		{
        	logger.error("put netbackup selections error!");
		}
        
        //System.out.println(result);
        
		return;
	}
	
	static void put_netbackup_client(String policyname, String hostname, String baseurl, String jwt)
	{
		String result = null;
		String clients = replaceclient(hostname);
		String url = baseurl + "/config/policies/" + policyname + "/clients/" + hostname;
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/vnd.netbackup+json;version=2.0");
        headers.set("Authorization", jwt);
        headers.set("Accept", "application/vnd.netbackup+json;version=2.0");
        RemoteRestClient client = new RemoteRestClient();
        try
        {
        	result = client.putRequestnoverify(clients, url, headers);
        }catch(Exception e)
		{
        	logger.error("put netbackup client error!");
		}
        //System.out.println(clients);
        
		return;
	}
	
	static void put_netbackup_schedule(String policyname, String schedulename,String retentionnum, String retentionperoid, String frequencytime, String weekday, String hour, String baseurl, String jwt)
	{
		String result = null;
		String schedule = replaceschedule(schedulename, retentionnum, retentionperoid, frequencytime, weekday, hour);
		
		String url = baseurl + "/config/policies/" + policyname + "/schedules/" + schedulename;
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/vnd.netbackup+json;version=2.0");
        headers.set("Authorization", jwt);
        headers.set("Accept", "application/vnd.netbackup+json;version=2.0");
        RemoteRestClient client = new RemoteRestClient();
        try
        {
        	result = client.putRequestnoverify(schedule, url, headers);
        }catch(Exception e)
		{
        	logger.error("put netbackup schedule error!");
		}
        //System.out.println(schedule);
        
		return;
	}
	static String get_netbackup_storagepool(String baseurl, String jwt)
	{
		String result = null;
		String res = null;
		String freesize;
		long size = 0;
		long oldsize = 0;
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Content-Type", "application/vnd.netbackup+json;version=2.0");
        headers.set("Authorization", jwt);
        headers.set("Accept", "application/vnd.netbackup+json;version=2.0");
        headers.set("X-NetBackup-Policy-Use-Generic-Schema", "true");
        String url = baseurl + "/storage/storage-units";
        RemoteRestClient client = new RemoteRestClient();
        
        try
        {
        	result = client.getRequestnoverify(null, url, headers);
        }catch(Exception e)
		{
        	logger.error("get netbackup storagepool error!");
			throw e;
		}
        
        JSONObject jsObj = JSON.parseObject(result);
        

        JSONArray json = (JSONArray) jsObj.get("data");
        if(json.size()>0)
        {
        	for(int i=0;i<json.size();i++)
        	{
	        	JSONObject job = json.getJSONObject(i); 
	        	//check status up
	        	if (((JSONObject)job.get("attributes")).get("storageServerState").equals("UP") && ((JSONObject)job.get("attributes")).get("diskPoolState").equals("UP"))
	        	{
	        		//System.out.println(((JSONObject)job.get("attributes")).get("storageUnitName").toString()) ;
	        		freesize = ((JSONObject)job.get("attributes")).get("freeCapacityBytes").toString();
	        		size = Long.parseLong(freesize);
	        		
	        		if (size >= oldsize)
	        		{
	        			res = ((JSONObject)job.get("attributes")).get("storageUnitName").toString();
	        		}
	        	}
	        	  
        	}
        }
        
		return res;
	}
	
	public static void main(String[] args) {
		try{
			String token = NBULoginin("https://10.96.93.74:1556/netbackup");
			
			//System.out.println(token);
			String liststorage = get_netbackup_storagepool("https://10.96.93.74:1556/netbackup", token);
			
			String policyname = post_netbackup_create_policy("mysqltest3", "Standard", liststorage, "https://10.96.93.74:1556/netbackup", token);
			put_netbackup_selections(policyname,"/data/mongo", "https://10.96.93.74:1556/netbackup", token);
			put_netbackup_client(policyname, "PEKLNBIGDT2", "https://10.96.93.74:1556/netbackup", token);
			put_netbackup_schedule(policyname,"schedule-test1", "1","WEEKS","weeks","Mon","16","https://10.96.93.74:1556/netbackup", token);
			put_netbackup_schedule(policyname,"schedule-test2", "1","WEEKS","weeks","Mon","1","https://10.96.93.74:1556/netbackup", token);
			
			NBULoginout(token);
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
