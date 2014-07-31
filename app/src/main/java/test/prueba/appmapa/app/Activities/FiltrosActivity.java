package test.prueba.appmapa.app.Activities;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.Filtros;
import test.prueba.appmapa.app.Dominio.Localidad;
import test.prueba.appmapa.app.Dominio.TipoMoneda;
import test.prueba.appmapa.app.Dominio.TipoOperacion;
import test.prueba.appmapa.app.Fragments.DialogTipoPropiedades;
import test.prueba.appmapa.app.Model.DataSpinnerModel;
import test.prueba.appmapa.app.Model.OperacionFiltroModel;
import test.prueba.appmapa.app.Parsers.LocalidadJSONParser;
import test.prueba.appmapa.app.R;
import test.prueba.appmapa.app.Utiles.DownLoader;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gbravo on 7/7/14.
 */
public class FiltrosActivity extends Activity implements DialogTipoPropiedades.OnPropiedadesSeleccionada, AdapterView.OnItemClickListener {

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String API_MELI_BASE = "https://mobile.mercadolibre.com.ar/sites/MLC/search?category=MLC1480" +
            "&state=TUxDUE1FVEExM2JlYg&city=TUxDQ1NBTjk4M2M";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyBuWERTOvSMXXiUggUVh6ZRMJ3jQghBybc";
    Filtros filtros;
    TextView txtTipoPropiedades;
    FragmentTransaction ft;
    DialogFragment dialogTipoPropiedades;
    AutoCompleteTextView autoCompView;

    List<DataSpinnerModel> listDataSpinner;
    ArrayAdapter<CharSequence> adapterPrecioDesde;
    ArrayAdapter<CharSequence> adapterHasta;
    Spinner spnPrecioDesde;
    Spinner spnPrecioHasta;
    List<DataSpinnerModel> listadoPrecio$;
    List<DataSpinnerModel> listadoPrecioUF;
    List<DataSpinnerModel> listadoActualPrecio;
    RadioGroup groupTipoOperacion;
    RadioGroup groupTipoMoneda;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filtros);

        ActionBar actionBar = getActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowCustomEnabled(true);

        View view = View.inflate(getApplicationContext(), R.layout.actionbar_filtros,
                null);
        actionBar.setCustomView(view);

        //ACA ES DONDE SE SETEA FILTROS
        Intent intent = getIntent();
        filtros = (Filtros) intent.getSerializableExtra("filtros");


        autoCompView = (AutoCompleteTextView)view.findViewById(R.id.action_filtros_autocomplete);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(getBaseContext(), android.R.layout.simple_list_item_1));

/*

        AutoCompleteTextView autoCompView = (AutoCompleteTextView)view.findViewById(R.id.action_filtros_autocomplete);

        PlacesAutoCompleteAdapter adapterAutoComplete = new PlacesAutoCompleteAdapter(getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item);
        //autoCompView.setDropDownBackgroundResource(R.drawable.button_selector);
        autoCompView.setAdapter(adapterAutoComplete);*/

        autoCompView.setOnItemClickListener(this);



        groupTipoOperacion = (RadioGroup)findViewById(R.id.groupTipoOperaciones);
        groupTipoOperacion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                OperacionFiltroModel op;
                switch (checkedId)
                {
                    case R.id.optVenta:
                        op = filtros.getOperacionPorTipo(TipoOperacion.Venta);
                        break;
                    case R.id.optArriendo:
                        op = filtros.getOperacionPorTipo(TipoOperacion.Arriendo);
                        break;
                    case R.id.optArriendoTemp:
                        op = filtros.getOperacionPorTipo(TipoOperacion.Arriendo_Temporada);
                        break;
                    default:
                        op = filtros.getOperacionPorTipo(TipoOperacion.Venta);
                        break;
                }

                checkGroupTipoMoneda(op.TipoMoneda);
                if(op.PrecioDesde != null) {
                    spnPrecioDesde.setSelection(op.PrecioDesde.getIndex());
                }else
                {
                    spnPrecioDesde.setSelection(0);
                }
                if(op.PrecioHasta != null)
                {
                    spnPrecioHasta.setSelection(op.PrecioHasta.getIndex());
                }else
                {
                    spnPrecioHasta.setSelection(0);
                }
            }
        });

        LinearLayout tipoPropiedad = (LinearLayout)findViewById(R.id.cmbTipoPropiedad);

        txtTipoPropiedades = (TextView)findViewById(R.id.txtTipoPropiedades);

        tipoPropiedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle arguments = new Bundle();
                arguments.putSerializable("filtros", filtros);

                DialogTipoPropiedades dialogTipoPropiedades = DialogTipoPropiedades.newInstance(arguments);
                ft = getFragmentManager().beginTransaction();
                dialogTipoPropiedades.show(ft, "dialog");
            }
        });

        RadioGroup groupDormitorios = (RadioGroup)findViewById(R.id.groupDormitorios);
        groupDormitorios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.optDorm0)
                {
                    filtros.setDormitorios(0);
                }else
                {
                    RadioButton rbDorm = (RadioButton)findViewById(checkedId);
                    String texto =  rbDorm.getText().subSequence(0, 1).toString();
                    Integer valor =  Integer.parseInt(texto);
                    filtros.setDormitorios(valor);

                }
            }
        });


        RadioGroup groupBanos = (RadioGroup)findViewById(R.id.groupBanos);
        groupBanos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.optBano0)
                {
                    filtros.setBanos(0);
                }else
                {
                    RadioButton rbBanos = (RadioButton)findViewById(checkedId);
                    String texto =  rbBanos.getText().subSequence(0, 1).toString();
                    Integer valor =  Integer.parseInt(texto);
                    filtros.setBanos(valor);

                }
            }
        });


        spnPrecioDesde = (Spinner)findViewById(R.id.spnrPrecioDesde);

        groupTipoMoneda = (RadioGroup)findViewById(R.id.groupTipoMoneda);
        groupTipoMoneda.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                DataSpinnerModel data;

                if(checkedId == R.id.optPesos)
                {
                    //filtros.setTipoMoneda(TipoMoneda.Pesos);
                    listadoActualPrecio = listadoPrecio$;
                    getOperacionSeleccionada().TipoMoneda = TipoMoneda.Pesos;


                }else {
                    //filtros.setTipoMoneda(TipoMoneda.UF);
                    listadoActualPrecio = listadoPrecioUF;
                    getOperacionSeleccionada().TipoMoneda = TipoMoneda.UF;
                }

                listDataSpinner = new ArrayList<DataSpinnerModel>();

                listDataSpinner.add(new DataSpinnerModel(0, "Desde",0));
                listDataSpinner.addAll(listadoActualPrecio);
                adapterPrecioDesde = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_item, listDataSpinner);
                adapterPrecioDesde.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnPrecioDesde.setAdapter(adapterPrecioDesde);

                if(getOperacionSeleccionada().PrecioHasta != null && getOperacionSeleccionada().PrecioHasta.getIndex() > 0)
                {
                    data = listadoActualPrecio.get(getOperacionSeleccionada().PrecioHasta.getIndex() -1);
                    getOperacionSeleccionada().PrecioHasta = data;
                }
                if(getOperacionSeleccionada().PrecioDesde != null && getOperacionSeleccionada().PrecioDesde.getIndex() > 0)
                {
                    data = listadoActualPrecio.get(getOperacionSeleccionada().PrecioDesde.getIndex() -1);
                    getOperacionSeleccionada().PrecioDesde = data;
                    spnPrecioDesde.setSelection(data.getIndex());
                }
            }
        });

        spnPrecioDesde.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                DataSpinnerModel data = (DataSpinnerModel) parent.getItemAtPosition(position);
                //filtros.setPrecioDesde(data);
                revisarSpinnerHasta(position, spnPrecioHasta, getOperacionSeleccionada().PrecioHasta, listadoActualPrecio);

                getOperacionSeleccionada().PrecioDesde = data;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnPrecioHasta = (Spinner)findViewById(R.id.spnrPrecioHasta);


        spnPrecioHasta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataSpinnerModel data = (DataSpinnerModel) parent.getItemAtPosition(position);
                //filtros.setPrecioHasta(data);

                getOperacionSeleccionada().PrecioHasta = data;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spnSuperficieDesde = (Spinner)findViewById(R.id.spnrSuperficieDesde);

        String[] superficiesDesde = getResources().getStringArray(R.array.superficies_array);
        superficiesDesde[0] = "Desde";
        ArrayAdapter<String> adapterSuperficieDesde = new ArrayAdapter(getBaseContext(),
                                                        android.R.layout.simple_spinner_item, superficiesDesde);
        adapterSuperficieDesde.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSuperficieDesde.setAdapter(adapterSuperficieDesde);

        Spinner spnSuperficieHasta = (Spinner)findViewById(R.id.spnrSuperficieHasta);

        String[] superficiesHasta = getResources().getStringArray(R.array.superficies_array);
        superficiesHasta[0] = "Hasta";
        ArrayAdapter<String> adapterSuperficieHasta = new ArrayAdapter(getBaseContext(),
                android.R.layout.simple_spinner_item, superficiesHasta);
        adapterSuperficieHasta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSuperficieHasta.setAdapter(adapterSuperficieHasta);

    }

    private OperacionFiltroModel getOperacionSeleccionada()
    {
        OperacionFiltroModel operacionFiltroModel;
        switch (groupTipoOperacion.getCheckedRadioButtonId())
        {
            case R.id.optVenta:
                operacionFiltroModel = filtros.getOperacionPorTipo(TipoOperacion.Venta);
                break;
            case R.id.optArriendo:
                operacionFiltroModel = filtros.getOperacionPorTipo(TipoOperacion.Arriendo);
                break;
            case R.id.optArriendoTemp:
                operacionFiltroModel = filtros.getOperacionPorTipo(TipoOperacion.Arriendo_Temporada);
                break;
            default:
                operacionFiltroModel = filtros.getOperacionPorTipo(TipoOperacion.Venta);
                break;
        }

        return operacionFiltroModel;
    }

    private void revisarSpinnerHasta(int positionDesde, Spinner spinnerHasta, DataSpinnerModel dataHasta, List<DataSpinnerModel> listado)
    {
        listDataSpinner = new ArrayList<DataSpinnerModel>();


        listDataSpinner.add(new DataSpinnerModel(0, "Hasta",0));

        int index = positionDesde == 0 ? positionDesde : positionDesde -1;

        for(int i = index; i < listado.size(); i++) {
            listDataSpinner.add(listado.get(i));
        }

        adapterHasta = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_item, listDataSpinner);
        adapterHasta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHasta.setAdapter(adapterHasta);

        if(dataHasta != null) {
            for (int i= 0; i < spinnerHasta.getCount(); i++)
            {
                if(spinnerHasta.getItemAtPosition(i).equals(dataHasta))
                {
                    spinnerHasta.setSelection(i);
                    break;
                }
            }

        }

    }

    void initArray()
    {
        String[] array$ =  getResources().getStringArray(R.array.precio_$_array);
        String[] arrayUF =  getResources().getStringArray(R.array.precio_UF_array);
        DataSpinnerModel dataSpinnerModel;

        DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("###,###.##",simbolo);

        listadoPrecio$ = new ArrayList<DataSpinnerModel>();

        Double valor;
        for (int i = 0; i < array$.length; i++)
        {

            valor = Double.parseDouble(array$[i]);
            dataSpinnerModel = new DataSpinnerModel(i + 1, "$" + formateador.format(valor), valor);
            listadoPrecio$.add(dataSpinnerModel);
        }

        listadoPrecioUF = new ArrayList<DataSpinnerModel>();

        for (int i = 0; i < arrayUF.length; i++)
        {
            valor = Double.parseDouble(arrayUF[i]);
            dataSpinnerModel = new DataSpinnerModel(i + 1, "UF " + formateador.format(valor), valor);
            listadoPrecioUF.add(dataSpinnerModel);
        }

    }
    @Override
    protected void onStart() {
        super.onStart();

        initArray();

        listadoActualPrecio = listadoPrecio$;

        if(filtros != null && filtros.getTiposPropiedad() != null) {
            txtTipoPropiedades.setText(TextUtils.join(", ", filtros.getTiposPropiedad()));
        }

        /*
        listDataSpinner = new ArrayList<DataSpinnerModel>();

        listDataSpinner.add(new DataSpinnerModel(0, "Desde",0));
        listDataSpinner.addAll(listadoPrecio$);

        adapterPrecioDesde = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_item, listDataSpinner);
        adapterPrecioDesde.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPrecioDesde.setAdapter(adapterPrecioDesde);
        */
        if(filtros.getTipoMoneda() != null) {
            getOperacionSeleccionada().TipoMoneda = filtros.getTipoMoneda();
            checkGroupTipoMoneda(filtros.getTipoMoneda());
        }else
        {
            checkGroupTipoMoneda(filtros.getOperacionPorTipo(TipoOperacion.Venta).TipoMoneda);
        }
    }

    private void checkGroupTipoMoneda(TipoMoneda tipoMoneda)
    {
        switch (tipoMoneda)
        {
            case Pesos:
                groupTipoMoneda.check(R.id.optPesos);
                break;
            case UF:
                groupTipoMoneda.check(R.id.optUF);
                break;
        }
    }

    private ArrayList<Localidad> autocomplete(String input) {
        ArrayList<Localidad> resultList = null;

        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?sensor=false&key=" + API_KEY);
            sb.append("&components=country:cl");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            String data = DownLoader.DownloadUrl(sb.toString());

            JSONObject jsonObj = new JSONObject(data.toString());
            resultList = LocalidadJSONParser.parse(jsonObj);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        Localidad localidad = (Localidad)adapterView.getItemAtPosition(position);

        filtros.setLocalidadReferencia(localidad.GetReferencia());

        /*
        IAsyncTaskDelegate delegado = new IAsyncTaskDelegate() {
            @Override
            public void onTaskComplete(JSONObject jsonObj) {
                try {
                    ejecutarLocalidadDetalle(jsonObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        };

        DownLoaderTask detaisTask = new DownLoaderTask(delegado);

        String reference = localidad.GetReferencia();

        StringBuilder url = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
        url.append("?sensor=false&key=" + API_KEY);
        url.append("&reference=" + reference);

        detaisTask.execute(url.toString());

        */

    }

    @Override
    public void onPropiedadesSeleccionada(String[] arrayString) {
        if(arrayString.length > 0) {
            txtTipoPropiedades.setText(TextUtils.join(", ", arrayString));
            filtros.setTiposPropiedad(arrayString);
        }else
        {
            txtTipoPropiedades.setText(null);
            filtros.setTiposPropiedad(null);
        }

    }


    private class PlacesAutoCompleteAdapter extends ArrayAdapter<Localidad> implements Filterable {
        private ArrayList<Localidad> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Localidad getItem(int index) {

            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {

                        resultList = autocomplete(constraint.toString());

                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtros,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_aceptar:

                Intent returnIntent = new Intent();
                returnIntent.putExtra("filtros", filtros);

                setResult(RESULT_OK, returnIntent);
                finish();

                return true;
            case R.id.action_cancelar:
                Intent cancelIntent = new Intent();
                setResult(RESULT_CANCELED, cancelIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
