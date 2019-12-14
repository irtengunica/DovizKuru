package com.example.dovizkuru;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DovizKuru extends Activity {
	public static final String URL = "http://www.doviz.gen.tr/doviz_json.asp?version=1.0.4";  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		setContentView(R.layout.doviz_kuru);
		// Gerekl� olan widgetlar�m�z� tan�tt�k
		final EditText tlcon=(EditText)findViewById(R.id.editText1); 
		final EditText dolarcon=(EditText)findViewById(R.id.editText2); 
		final EditText eurocon=(EditText)findViewById(R.id.editText3);
	
		//program ba�lat�ld�g�nda verilerin g�ncellenmesi i�in kulland�k
		UpdateData();
		//G�ncelle butonunun dinleyicisi
		Button btnupt=(Button)findViewById(R.id.buttonGnc);
		btnupt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UpdateData();
				
			}
		});
		 final EditText dolar_1=(EditText)findViewById(R.id.editTextDolar1); 
		   final EditText dolar_2=(EditText)findViewById(R.id.editTextDolar2); 
		   final EditText euro_1=(EditText)findViewById(R.id.editTextEuro1); 
		   final EditText euro_2=(EditText)findViewById(R.id.editTextEuro2);
		   TextView UptadeTime = (TextView) findViewById(R.id.textViewTime1);
//NumberFormatException hatas� almamak i�in birim �eviri k�sm�ndak� EditText de�erlerini "0" at�yoruz
		tlcon.setText("0");
		dolarcon.setText("0");
		eurocon.setText("0");
		final float tlcon1=Float.parseFloat(tlcon.getText().toString());
		final float dolarcon1=Float.parseFloat(dolarcon.getText().toString());
		final float eurocon1=Float.parseFloat(eurocon.getText().toString());
	//�eviri tu�unun dinleyicisi
		Button btnconvert =(Button)findViewById(R.id.buttonConvert);
	btnconvert.setOnClickListener(new View.OnClickListener() {
		
		@Override
	//Birimleri birbirine �evirmek i�in kullan�lan kodlar
		public void onClick(View v) {
			float tlcon1=Float.parseFloat(tlcon.getText().toString());
			float dolarcon1=Float.parseFloat(dolarcon.getText().toString());
			float eurocon1=Float.parseFloat(eurocon.getText().toString());
			
			if(tlcon1!=0&&dolarcon1==0&&eurocon1==0){
				dolarcon1=tlcon1/Float.parseFloat(dolar_1.getText().toString());
				eurocon1=tlcon1/Float.parseFloat(euro_1.getText().toString());
				dolarcon.setText(Float.toString(dolarcon1));
				eurocon.setText(Float.toString(eurocon1));
				
			}
			else if(dolarcon1!=0&&tlcon1==0&&eurocon1==0){tlcon1=dolarcon1*Float.parseFloat(dolar_2.getText().toString());
			        eurocon1=dolarcon1*Float.parseFloat(dolar_2.getText().toString())/Float.parseFloat(euro_1.getText().toString());
			    	tlcon.setText(Float.toString(tlcon1));
			    	eurocon.setText(Float.toString(eurocon1));}
			//Eger birden fazla birim girildiyse yanl�� deger almamak �c�n EditTextleri s�f�rl�yoruz
			else if(eurocon1!=0&&tlcon1==0&&dolarcon1==0){tlcon1=eurocon1*Float.parseFloat(euro_2.getText().toString());
			       dolarcon1=eurocon1*Float.parseFloat(euro_2.getText().toString())/Float.parseFloat(dolar_1.getText().toString());
			   	tlcon.setText(Float.toString(tlcon1));
				eurocon.setText(Float.toString(eurocon1));}else{
					tlcon.setText("0");
					dolarcon.setText("0");
					eurocon.setText("0");}
		}
		
	});
		//��k�� tusunun dinleyicisi
		Button btnexit=(Button)findViewById(R.id.buttonExit);
		btnexit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DovizKuru.this.finish();
			}
		});
		
	}


	
	public   void UpdateData(){
		//loyout uzerindeki bile�enleri tan�mlayaca��z
		   EditText dolar_1=(EditText)findViewById(R.id.editTextDolar1); 
		   EditText dolar_2=(EditText)findViewById(R.id.editTextDolar2); 
		   EditText euro_1=(EditText)findViewById(R.id.editTextEuro1); 
		   EditText euro_2=(EditText)findViewById(R.id.editTextEuro2);
		   TextView UptadeTime = (TextView) findViewById(R.id.textViewTime1);
		   
		   
		   
		   String dolar ;
	    String euro   ;
		   String dolar2 ;
		     String euro2 ;
		    String LastUptadeTime;  
		     String LastRegisterDate ;
		//burada bir web iste�i olu�turuyoruz
		HttpClient client =new DefaultHttpClient();
		HttpGet  getData =new HttpGet(URL);
		HttpResponse response;
		try{
			
			response=client.execute(getData);
			HttpEntity entity=response.getEntity();
			if(entity != null){
				 
				InputStream Data=entity.getContent();
				String result =convertStreamToString(Data);
				//Siteden gelen veriyi JSON Objemize at�yoruz
				JSONObject jsonVeri=new JSONObject(result); 
				dolar = jsonVeri.getString("dolar2");  
				euro = jsonVeri.getString("euro2");  
                dolar2 = jsonVeri.getString("dolar");  
                euro2 = jsonVeri.getString("euro");  
                LastUptadeTime = jsonVeri.getString("guncelleme");  
                LastRegisterDate = jsonVeri.getString("sonkayit"); 
              
                  //Verilerimizi ekrana yazd�r�yoruz
                   dolar_1.setText(dolar.toString());  
                   euro_1.setText(euro.toString());  
                   dolar_2.setText(dolar2.toString());  
                   euro_2.setText(euro2.toString());  
                   UptadeTime.setText(LastUptadeTime.toString()+"/n"+LastRegisterDate.toString());  
                
                  
                  
                 }
			
		}catch(Exception e){ LastUptadeTime="G�ncelleme ba�ar�s�z!\nInternet ba�lant�n�z� kontrol edip, tekrar deneyiniz!";  
		
         }
		}
	public static String convertStreamToString(InputStream is){
		//burada gelen veriyi string de�erine �evirece�iz
		 BufferedReader reader = new BufferedReader(new InputStreamReader(is));  
	        StringBuilder sb = new StringBuilder();  
	        String line = null;  
	        try {  
	          while ((line = reader.readLine()) != null) {  
	              sb.append(line).append("\n");  
	          }  
	        } catch (IOException e) {  
	        } finally {  
	          try {  
	              is.close();  
	          } catch (IOException e) {  
	          }  
	        }  
	        return sb.toString();  
	    }  
			
	 }

