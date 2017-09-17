package bemypet.com.br.bemypet_v1.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.R;
import bemypet.com.br.bemypet_v1.VisualizarAdocaoAprovadaActivity;
import bemypet.com.br.bemypet_v1.VisualizarAdocaoReprovadaActivity;
import bemypet.com.br.bemypet_v1.VisualizarDenunciaActivity;
import bemypet.com.br.bemypet_v1.VisualizarSolicitacaoAdocaoActivity;
import bemypet.com.br.bemypet_v1.adapters.NotificacoesAdapter;
import bemypet.com.br.bemypet_v1.adapters.SwipeListViewAdapter;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.pojo.Usuario;
import bemypet.com.br.bemypet_v1.utils.Constants;
import bemypet.com.br.bemypet_v1.utils.Utils;

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
    private SwipeListViewAdapter mAdapter;
    private Usuario usuarioLogado = new Usuario();

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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSearchView != null) {
            mSearchView.setQuery("", false);
            mSearchView.clearFocus();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_notificacoes_mensagens, container, false);
        mSearchView = (SearchView) rootView.findViewById(R.id.searchViewNotificacoes);
        mListView = (ListView) rootView.findViewById(R.id.listViewNotificacoes);
        notificacoesList = new ArrayList<Notificacoes>();
        mAdapter = new SwipeListViewAdapter(getContext(), notificacoesList);

        mAdapter.setMode(Attributes.Mode.Single);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Notificacoes n = notificacoesList.get(position);
                if(!n.lida) {
                    n.lida = Boolean.TRUE;
                    updateNotificacao(n);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("notificacao", new Gson().toJson(n));
                Intent intent = null;

                if(n.topico.equalsIgnoreCase(Constants.TOPICO_SOLICITACAO_ADOCAO)) {
                    intent = new Intent(getContext(), VisualizarSolicitacaoAdocaoActivity.class);
                }

                if(n.topico.equalsIgnoreCase(Constants.TOPICO_ADOÇÃO_APROVADA)) {
                    intent = new Intent(getContext(), VisualizarAdocaoAprovadaActivity.class);
                }

                if(n.topico.equalsIgnoreCase(Constants.TOPICO_ADOÇÃO_REPROVADA)) {
                    intent = new Intent(getContext(), VisualizarAdocaoReprovadaActivity.class);
                }

                if(n.topico.equalsIgnoreCase(Constants.TOPICO_DENUNCIA)) {
                    intent = new Intent(getContext(), VisualizarDenunciaActivity.class);
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });

        mListView.setTextFilterEnabled(true);
        setupSearchView();
        if(Utils.getUsuarioSharedPreferences(getContext()) != null) {
            setUsuarioLogado(Utils.getUsuarioSharedPreferences(getContext()));
            buscarNotificacoes();
        }
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

    private void buscarNotificacoes() {

        FirebaseDatabase.getInstance().getReference().child("notificacoes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Notificacoes notificacao = null;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    notificacao = postSnapshot.getValue(Notificacoes.class);
                    if(notificacao.destinatarioId.equalsIgnoreCase(getUsuarioLogado().id)) {
                        notificacoesList.add(notificacao);
                    }
                }
                //notificacoesAdapter = new NotificacoesAdapter(NotificacoesMensagensFragment.this.getActivity(), notificacoesList);
                mAdapter = new SwipeListViewAdapter(getContext(), notificacoesList);
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }



    private void updateNotificacao(Notificacoes n) {
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("notificacoes");
        myRef.child(n.id).child("lida").setValue(n.lida);
    }

    private void setupSearchView()  {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Buscar");
        mSearchView.setIconified(false);
        if (mSearchView != null) {
            mSearchView.setQuery("", false);
            mSearchView.clearFocus();
        }
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
