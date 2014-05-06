package ro.pub.cs.brtalk;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HelloWorkd extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello_workd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hello_workd, menu);
		return true;
	}

}
