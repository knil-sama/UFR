package fr.areaX.biometry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class IrisScanProcessor implements IrisScanInterface {

	private ProcessBuilder processbuilder;
	
	private static final String nativeProgramPath = "native/mock";
	private static final String imgRef = "1";
	
	public IrisScanProcessor() {
	}
	
	@Override
	public JSONObject parseImage(String imageUrl) {
		try {
			Process cprocess = new ProcessBuilder(nativeProgramPath, imageUrl, imgRef).start();

			InputStream inputStream = cprocess.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream);
			BufferedReader bfr = new BufferedReader(isr);
			
			String line;
			String buffer = "";
			while((line=bfr.readLine())!=null){
				buffer += line;
			}
			return (new JSONObject(buffer));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (new JSONObject("{'reply': 'Java RunTime Exception'}"));
	}
	
	public static void test() {
		IrisScanProcessor iris = new IrisScanProcessor();
		
		JSONObject histo = iris.parseImage("");
		
		System.out.println(histo.toString());
	}

}
