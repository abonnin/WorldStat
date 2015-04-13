package org.joliverie.sio.slam.worldstat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class WorldStatsActivity extends Activity implements
		OnItemSelectedListener, OnItemClickListener {

	private static final String COUNTRIES_EMPTY = "<countries><country><name>Vide!</name></country></countries>";

	// 10.0.2.2 : pour atteindre une ressource sur la machine hôte de l'AVD
	// private static final String URL_XMLRESSOURCE =
	// "http://192.168.0.3/projects/World2/xmlCountries.php?continent=";
	private static final String URL_XMLRESSOURCE = "http://10.0.2.2/World2/xmlCountries.php?continent=";

	private Spinner spinContinents;
	private ListView listViewPays;
	private ArrayList<Pays> desPays;
	private ReqHTTPTask reqHTTPTask = null;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_world_stats);

		// obtenir les références des instances de widgets désérialisés
		this.spinContinents = (Spinner) findViewById(R.id.continents);
		this.listViewPays = (ListView) findViewById(R.id.despays);
		this.desPays = new ArrayList<Pays>();

		// ArrayAdapter : permet de remplir les données d'un TextView
		// param1 : le contexte
		// param2 : l'id du textView
		// param3 : les données = une list de Pays
		this.listViewPays.setAdapter(new ArrayAdapter<Pays>(this,
				R.layout.list_item, desPays));
		this.listViewPays.setOnItemClickListener(this);

		// mise en place du "modèle" de spinContinents
		// Crée un nouvel ArrayAdapter Ã  partir d'une ressource
		// param1 : le contexte
		// param2 : les donnÃ©es qui viennent d'une ressource tableau =t ableau
		// de continents
		// param3 : l'id du Spinner
		// CharSequence = interface séquence de caractÃ¨res ? On aurait pu
		// mettre String ?
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.continents, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinContinents.setAdapter(adapter);
		spinContinents.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view,
			int position, long id) {

		if (adapterView == this.spinContinents) {
			if (reqHTTPTask != null) {
				return;
			}
			// instanciation et lancement de la tâche asynchroone de récupération des pays dans la BDD
			reqHTTPTask = new ReqHTTPTask();
			reqHTTPTask.execute((Void) null);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		Toast.makeText(
				getApplicationContext(),
				"Pays sélectionné : "
						+ listViewPays.getAdapter().getItem(position),
				Toast.LENGTH_LONG).show();

		Bundle bundle = new Bundle();
		bundle.putString("pays", listViewPays.getAdapter().getItem(position)
				.toString());
		Intent detailIntent = new Intent(getApplicationContext(),
				PaysDetailActivity.class);
		detailIntent.putExtras(bundle);
		startActivity(detailIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_world_stats, menu);
		return true;
	}
	
	// Début définition classe ReqHTTPTask
	public class ReqHTTPTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {

				// modification de la liste des pays
				desPays.clear();
				UtilWorldXml.parseListePays(URL_XMLRESSOURCE + URLEncoder.encode(spinContinents.getSelectedItem().toString(),"UTF-8"), 
						COUNTRIES_EMPTY, desPays);
				return true;
			} catch (UnsupportedEncodingException e) {
				return false;
			}
		}


		@Override
		protected void onPostExecute(final Boolean success) {
			reqHTTPTask = null;
			if (success) {
				Toast.makeText(getApplicationContext(),
						"Nombre de pays :" + desPays.size(), Toast.LENGTH_LONG)
						.show();

				@SuppressWarnings("unchecked")
				ArrayAdapter<Pays> modelDeListePays = (ArrayAdapter<Pays>) listViewPays
						.getAdapter();

				modelDeListePays.notifyDataSetChanged();

				Toast.makeText(getApplicationContext(), "Succès : " + success,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"erreur de chargement des pays", Toast.LENGTH_LONG)
						.show();

			}
		}
	} // fin définition classe ReqHTTPTask
}
