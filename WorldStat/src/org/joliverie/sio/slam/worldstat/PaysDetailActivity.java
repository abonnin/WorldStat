package org.joliverie.sio.slam.worldstat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PaysDetailActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.pays_detail);
    String pays = getIntent().getExtras().getString("pays");
    TextView tv = (TextView) findViewById(R.id.pays_name);
    tv.setText(pays);
  }

}

