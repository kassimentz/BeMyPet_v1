package bemypet.com.br.bemypet_v1.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import bemypet.com.br.bemypet_v1.PerfilPetActivity;
import bemypet.com.br.bemypet_v1.R;
import bemypet.com.br.bemypet_v1.VisualizarSolicitacaoAdocaoActivity;
import bemypet.com.br.bemypet_v1.adapters.NotificacoesAdapter;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificacoesMensagensFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificacoesMensagensFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificacoesMensagensFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View rootView;

    private SearchView mSearchView;
    private ListView mListView;
    private List<Notificacoes> notificacoesList;
    private NotificacoesAdapter notificacoesAdapter;

    private OnFragmentInteractionListener mListener;

    public NotificacoesMensagensFragment() {
    }

    public static NotificacoesMensagensFragment newInstance(String param1, String param2) {

        NotificacoesMensagensFragment fragment = new NotificacoesMensagensFragment();
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

        buscarNotificacoes();
    }

    private void buscarNotificacoes() {

        FirebaseDatabase.getInstance().getReference().child("notificacoes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO mostrar soment notificacoes do usuario logado
                Notificacoes notificacao = null;
                notificacoesList = new ArrayList<Notificacoes>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    notificacao = postSnapshot.getValue(Notificacoes.class);
                    notificacoesList.add(notificacao);
                }


                notificacoesAdapter = new NotificacoesAdapter(NotificacoesMensagensFragment.this.getActivity(), notificacoesList);
                mListView.setAdapter(notificacoesAdapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                        Intent intent = new Intent(getContext(), VisualizarSolicitacaoAdocaoActivity.class);
                        Notificacoes n = notificacoesList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("adocao", new Gson().toJson(n.adocao));
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });

                mListView.setTextFilterEnabled(true);
                setupSearchView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    private void setupSearchView()  {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Buscar");
        mSearchView.setIconified(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_notificacoes_mensagens, container, false);
        mSearchView = (SearchView) rootView.findViewById(R.id.searchViewNotificacoes);
        mListView = (ListView) rootView.findViewById(R.id.listViewNotificacoes);
        return  rootView;
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText);
        }
        return true;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
