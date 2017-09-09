package bemypet.com.br.bemypet_v1.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.R;
import bemypet.com.br.bemypet_v1.pojo.Notificacoes;
import bemypet.com.br.bemypet_v1.utils.Constants;

/**
 * Created by kassianesmentz on 03/09/17.
 */

public class NotificacoesAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public List<Notificacoes> notificacoesList;
    public List<Notificacoes> notificacoes;

    public NotificacoesAdapter(Context context, List<Notificacoes> notificacoesList) {
        super();
        this.context = context;
        this.notificacoesList = notificacoesList;
    }


    public class NotificacoesHolder
    {
        TextView txtTopicoMensagem, txtTituloMensagem, txtMensagem, txtDataMensagem, txtStatusNotificacao;
        ImageView imgStatusNotificacao;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Notificacoes> results = new ArrayList<>();
                if (notificacoes == null)
                    notificacoes = notificacoesList;
                if (constraint != null) {
                    //TODO DEPOIS FILTRAR SOMENTE PELAS NOTIFICACOES DO USUARIO LOGADO NO APARELHO
                    if (notificacoes != null && notificacoes.size() > 0) {
                        for (final Notificacoes not : NotificacoesAdapter.this.notificacoes) {
                            if (not.adocao.adotante.nome.toLowerCase().contains(constraint.toString()))
                                results.add(not);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                notificacoesList = (List<Notificacoes>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return notificacoesList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificacoesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificacoesHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_list_notificacoes, parent, false);
            holder = new NotificacoesHolder();
            holder.txtTopicoMensagem = (TextView) convertView.findViewById(R.id.txtTopicoMensagem);
            holder.txtTituloMensagem = (TextView) convertView.findViewById(R.id.txtTituloMensagem);
            holder.txtMensagem = (TextView) convertView.findViewById(R.id.txtMensagem);
            holder.txtDataMensagem = (TextView) convertView.findViewById(R.id.txtDataMensagem);
            holder.txtStatusNotificacao = (TextView) convertView.findViewById(R.id.txtStatusNotificacao);
            holder.imgStatusNotificacao = (ImageView) convertView.findViewById(R.id.imgStatusNotificacao);
            convertView.setTag(holder);
        } else {
            holder = (NotificacoesHolder) convertView.getTag();
        }

        holder.txtTopicoMensagem.setText(notificacoesList.get(position).topico);
        holder.txtTituloMensagem.setText(notificacoesList.get(position).titulo);
        holder.txtMensagem.setText(notificacoesList.get(position).mensagem);
        holder.txtDataMensagem.setText(notificacoesList.get(position).data);
        holder.txtStatusNotificacao.setText(notificacoesList.get(position).statusNotificacao);

        if(notificacoesList.get(position).statusNotificacao.equalsIgnoreCase(Constants.REQUER_RESPOSTA) ||
                notificacoesList.get(position).statusNotificacao.equalsIgnoreCase(Constants.ENVIADA) ||
                notificacoesList.get(position).statusNotificacao.equalsIgnoreCase(Constants.ERRO)) {
            holder.imgStatusNotificacao.setImageResource(R.drawable.bg_circle);
        } else {
            holder.imgStatusNotificacao.setImageResource(R.drawable.round_green);
        }

        if(notificacoesList.get(position).lida) {
            holder.txtTituloMensagem.setTypeface(holder.txtTituloMensagem.getTypeface(), Typeface.NORMAL);
            holder.txtTopicoMensagem.setTypeface(holder.txtTopicoMensagem.getTypeface(), Typeface.NORMAL);
        } else {
            holder.txtTituloMensagem.setTypeface(holder.txtTituloMensagem.getTypeface(), Typeface.BOLD);
            holder.txtTopicoMensagem.setTypeface(holder.txtTopicoMensagem.getTypeface(), Typeface.BOLD);
        }

        return convertView;

    }
}
