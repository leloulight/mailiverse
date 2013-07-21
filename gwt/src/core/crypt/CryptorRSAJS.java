package core.crypt;

import java.io.IOException;
import java.io.InputStream;

import app.service.JSInvoker;

import com.google.gwt.core.client.JavaScriptObject;

import core.util.Base64;
import core.util.LogNull;
import core.util.LogOut;
import core.callback.Callback;
import core.callback.CallbackDefault;
import core.util.Streams;

public class CryptorRSAJS extends CryptorRSA 
{
	static LogNull log = new LogNull(CryptorRSAJS.class);
	
	JavaScriptObject privateKey, publicKey;
	
	protected void initialize (byte[] publicKeyBytes, byte[] privateKeyBytes) throws IOException 
	{
		super.initialize(publicKeyBytes, privateKeyBytes);

		if (privateKeyBytes != null)
			privateKey = jsGetPrivKey(Base64.encode(privateKeyBytes));

		if (publicKeyBytes != null)
			publicKey = jsGetPubKey(Base64.encode(publicKeyBytes));
	}
	
	public CryptorRSAJS(InputStream pri, InputStream pub) throws IOException 
	{
		initialize(
			pub != null ? Streams.readFullyBytes(pub) : null,
			pri != null ? Streams.readFullyBytes(pri) : null
		);
	}
	
	public CryptorRSAJS(byte[] publicKeyBytes, byte[] privateKeyBytes) throws IOException 
	{
		initialize(publicKeyBytes, privateKeyBytes);
	}

	//------------------------------------------------------------
	
	public Callback jsEncrypt_()
	{
		return new CallbackDefault() {
			public void onSuccess(Object... arguments) throws Exception {
				log.debug("jsEncrypt_",arguments[0]);
				jsEncrypt(publicKey, (String)(arguments[0]), JSInvoker.wrap(callback));
			}
		};
	}
	
	public Callback jsDecrypt_()
	{
		return new CallbackDefault() {
			public void onSuccess(Object... arguments) throws Exception {
				log.debug("jsDecrypt_", arguments[0]);
				jsDecrypt(privateKey, (String)(arguments[0]), JSInvoker.wrap(callback));
			}
		};
	}

	//------------------------------------------------------------
	
	public native JavaScriptObject jsGetPrivKey (String pem64) /*-{
		return $wnd.mSupport.rsa_getPrivateKey(pem64);
	}-*/;
	
	public native JavaScriptObject jsGetPubKey (String pem64) /*-{
		return $wnd.mSupport.rsa_getPublicKey(pem64);
	}-*/;
	
	public native void jsEncrypt(JavaScriptObject key, String bytes64, JavaScriptObject callback) /*-{
		return $wnd.mAsync.rsa_encrypt(callback, key, bytes64);
	}-*/;
	
	public native void jsDecrypt(JavaScriptObject key, String bytes64, JavaScriptObject callback) /*-{
		return $wnd.mAsync.rsa_decrypt(callback, key, bytes64);
	}-*/;
}
