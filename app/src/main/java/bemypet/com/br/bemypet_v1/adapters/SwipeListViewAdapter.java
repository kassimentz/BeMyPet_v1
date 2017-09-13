package bemypet.com.br.bemypet_v1.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

import bemypet.com.br.bemypet_v1.R;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.utils.Constants;

/**
 * Created by kassianesmentz on 11/09/17.
 */

public class SwipeListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    public List<Notificacoes> notificacoes;

    public SwipeListViewAdapter(Context mContext, List<Notificacoes> notificacoes) {
        this.notificacoes = notificacoes;
        this.mContext = mContext;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.swipe_list_view_item, null);
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));

        if(swipeLayout.isClickToClose()) {
            notifyDataSetChanged();
        }

        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
            }
        });

        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(notificacoes.get(position).toString());
                notificacoes.remove(notificacoes.get(position));
                final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("notificacoes");


                myRef.child(notificacoes.get(position).id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Open) {
                                //DO WHAT YOU WANT

                                notifyDataSetChanged();
                                swipeLayout.close();
                            }
                        }
                    }
                });
                notifyDataSetChanged();
            }

        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {

        TextView txtTopicoMensagem = (TextView) convertView.findViewById(R.id.txtTopicoMensagem);
        TextView txtTituloMensagem = (TextView) convertView.findViewById(R.id.txtTituloMensagem);
        TextView txtMensagem = (TextView) convertView.findViewById(R.id.txtMensagem);
        TextView txtDataMensagem = (TextView) convertView.findViewById(R.id.txtDataMensagem);
        TextView txtStatusNotificacao = (TextView) convertView.findViewById(R.id.txtStatusNotificacao);
        ImageView imgStatusNotificacao = (ImageView) convertView.findViewById(R.id.imgStatusNotificacao);
//        TextView t = (TextView)convertView.findViewById(R.id.txtNomeAdotante);
//        t.setText((position + 1) + ".");

        txtTopicoMensagem.setText(notificacoes.get(position).topico);
        txtTituloMensagem.setText(notificacoes.get(position).titulo);
        txtMensagem.setText(notificacoes.get(position).mensagem);
        txtDataMensagem.setText(notificacoes.get(position).data);
        txtStatusNotificacao.setText(notificacoes.get(position).statusNotificacao);

        if(notificacoes.get(position).statusNotificacao != null) {
            if (notificacoes.get(position).statusNotificacao.equalsIgnoreCase(Constants.STATUS_NOTIFICACAO_REQUER_RESPOSTA) ||
                    notificacoes.get(position).statusNotificacao.equalsIgnoreCase(Constants.STATUS_NOTIFICACAO_ENVIADA) ||
                    notificacoes.get(position).statusNotificacao.equalsIgnoreCase(Constants.STATUS_NOTIFICACAO_ERRO)) {
                imgStatusNotificacao.setImageResource(R.drawable.bg_circle);
            } else {
                imgStatusNotificacao.setImageResource(R.drawable.round_green);
            }
        }

        if(notificacoes.get(position).lida) {
            txtTituloMensagem.setTypeface(txtTituloMensagem.getTypeface(), Typeface.NORMAL);
            txtTopicoMensagem.setTypeface(txtTopicoMensagem.getTypeface(), Typeface.NORMAL);
        } else {
            txtTituloMensagem.setTypeface(txtTituloMensagem.getTypeface(), Typeface.BOLD);
            txtTopicoMensagem.setTypeface(txtTopicoMensagem.getTypeface(), Typeface.BOLD);
        }

    }



    @Override
    public int getCount() {
        return notificacoes.size();
    }

    @Override
    public Object getItem(int i) {
        return notificacoes.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
