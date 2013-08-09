/**
 * Author: Timothy Prepscius
 * License: GPLv3 Affero + keep my name in the code!
 */

package core.callback;


public class CallbackSync<T> 
{
	CallbackChain chain;
	Object[] results;
	
	public CallbackSync(Callback callback)
	{
		chain = callback.addCallback(setResults_());
	}
	
	public CallbackSync<T> invoke (Object...args)
	{
		chain.invoke(args);
		return this;
	}
	
	public Callback setResults_()
	{
		return new Callback() {
			public void invoke(Object... arguments) {
				results = arguments;
			}
		};
	}
	
	public T exportNoException ()
	{
		try
		{
			return export();
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public T export() throws Exception
	{
		if (results != null && results.length > 0)
		{
			if (results[0] instanceof Exception)
			{
				Exception e = (Exception)results[0];
				throw e;
			}
			
			return (T)results[0];
		}
		
		return null;
	}
}