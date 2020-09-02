package com.arima.healthyliving.voice;


import com.arima.healthyliving.R;
import com.example.usesqlite.MainActivity1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FunctionButton extends Activity{
	private Button shouzhiButton;
	private Button booksButton;
	private Button libraryButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.functionbutton);
		shouzhiButton = (Button)findViewById(R.id.shouzhi);
		booksButton = (Button)findViewById(R.id.books);
		libraryButton = (Button)findViewById(R.id.btnFindbase);
		shouzhiButton.setText(R.string.other0);
		libraryButton.setText(R.string.other4);
		booksButton.setText(R.string.other7);
		
		shouzhiButton.setOnClickListener(new ShouzhiListener());
		booksButton.setOnClickListener(new BooksListener());
		  libraryButton.setOnClickListener(new LibraryListener());
	}
	
class ShouzhiListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		
		intent.setClass(FunctionButton.this, MainActivity.class);
		FunctionButton.this.startActivity(intent);
	}
	
}
class BooksListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(FunctionButton.this, BarCodeTestActivity.class);
		FunctionButton.this.startActivity(intent);
		
		
	}
	
}
class LibraryListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(FunctionButton.this, MainActivity1.class);
		FunctionButton.this.startActivity(intent);
		
	}
	
}
}
