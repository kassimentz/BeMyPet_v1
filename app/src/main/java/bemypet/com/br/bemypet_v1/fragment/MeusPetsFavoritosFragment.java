package bemypet.com.br.bemypet_v1.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import bemypet.com.br.bemypet_v1.CadastroPetActivity;
import bemypet.com.br.bemypet_v1.CadastroUsuarioActivity;
import bemypet.com.br.bemypet_v1.FiltrosActivity;
import bemypet.com.br.bemypet_v1.PerfilUsuarioActivity;
import bemypet.com.br.bemypet_v1.R;
import bemypet.com.br.bemypet_v1.adapters.MeusPetsFavoritosAdapter;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.utils.Constants;
import bemypet.com.br.bemypet_v1.utils.Utils;

public class MeusPetsFavoritosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View rootView;
    private ListView listViewMeusPets;
    private ListView listViewPetsFavoritos;
    private MeusPetsFavoritosAdapter meusPetsAdapter;
    private MeusPetsFavoritosAdapter petsFavoritosAdapter;

    private List<Pet> meusPets;
    private List<Pet> meusFavoritos;

    private Usuario usuarioLogado;

    private OnFragmentInteractionListener mListener;

    public MeusPetsFavoritosFragment() {}

    public static MeusPetsFavoritosFragment newInstance(String param1, String param2) {
        MeusPetsFavoritosFragment fragment = new MeusPetsFavoritosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_meus_pets_favoritos, container, false);
        listViewMeusPets = (ListView) rootView.findViewById(R.id.listViewMeusPets);
        listViewPetsFavoritos = (ListView) rootView.findViewById(R.id.listViewPetsFavoritos);

        meusPets = new ArrayList<>();
        meusFavoritos = new ArrayList<>();

        Usuario tmpUsuario = Utils.getUsuarioSharedPreferences(getContext());
        if(tmpUsuario != null) {
            setUsuarioLogado(tmpUsuario);
            if(getUsuarioLogado().petsFavoritos != null && getUsuarioLogado().petsFavoritos.size() > 0) {
                meusFavoritos = getUsuarioLogado().petsFavoritos;
                petsFavoritosAdapter = new MeusPetsFavoritosAdapter(getContext(),meusFavoritos);
                listViewPetsFavoritos.setAdapter(petsFavoritosAdapter);
                petsFavoritosAdapter.notifyDataSetChanged();
            }
        }

        buscaMeusPets();
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pets, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.novo_pet:
                Intent intent;
                if(!getUsuarioLogado().getLogradouro().isEmpty()) {
                    intent = new Intent(getContext(), CadastroPetActivity.class);
                } else {
                    Utils.showToastMessage(getContext(), "Para cadastrar um pet, primeiro complete seus dados: ");
                    intent = new Intent(getContext(), CadastroUsuarioActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("origem", "cadastroPet");
                    intent.putExtras(bundle);
                }
                startActivity(intent);
                return true;

            default:
                break;
        }

        return false;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void buscaMeusPets() {
        //buscar os pets do usuario e setar no adapter

        FirebaseDatabase.getInstance().getReference().child("pets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pet pet = null;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    pet = postSnapshot.getValue(Pet.class);
                    if(pet.atualDonoID.equalsIgnoreCase(getUsuarioLogado().id)) {
                        meusPets.add(pet);
                    }
                }
                meusPetsAdapter = new MeusPetsFavoritosAdapter(getContext(), meusPets);
                listViewMeusPets.setAdapter(meusPetsAdapter);
                meusPetsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
