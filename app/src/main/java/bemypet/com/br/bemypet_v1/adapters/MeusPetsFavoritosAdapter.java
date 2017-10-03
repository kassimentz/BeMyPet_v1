package bemypet.com.br.bemypet_v1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.R;
import bemypet.com.br.bemypet_v1.pojo.Pet;

/**
 * Created by kassianesmentz on 17/09/17.
 */

public class MeusPetsFavoritosAdapter extends BaseAdapter {

    public Context context;
    public List<Pet> pets;

    public MeusPetsFavoritosAdapter(Context context, List<Pet> pets) {
        this.context = context;
        if(pets != null) {
            this.pets = pets;
        } else {
            this.pets = new ArrayList<>();
        }
    }

    public class MeusPetsFavoritosHolder {
        ImageView imgPet;
        TextView txtNomePet, txtDadosPet;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pets.size();
    }

    @Override
    public Pet getItem(int i) {
        return pets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        MeusPetsFavoritosHolder holder;


            convertView = LayoutInflater.from(context).inflate(R.layout.row_list_pet, parent, false);
            holder = new MeusPetsFavoritosHolder();
            holder.imgPet = (ImageView) convertView.findViewById(R.id.listImgPet);
            holder.txtNomePet = (TextView) convertView.findViewById(R.id.txtNomePet);
            holder.txtDadosPet = (TextView) convertView.findViewById(R.id.txtDadosPet);


        if(pets.size() > 0) {
            if (pets.get(i).imagens != null && pets.get(i).imagens.size() > 0) {
                Glide.with(context).load(pets.get(i).imagens.get(0)).apply(RequestOptions.circleCropTransform()).into(holder.imgPet);
            }

            holder.txtNomePet.setText(pets.get(i).nome);
            StringBuilder dadosPet = new StringBuilder();
            //Espécie (raça), sexo, idade
            dadosPet.append(pets.get(i).especie);
            dadosPet.append(" (" + pets.get(i).raca + "), ");
            dadosPet.append(pets.get(i).sexo + ", ");
            dadosPet.append(pets.get(i).idadeAproximada+ ", ");
            if(pets.get(i).cadastroAtivo) {
                dadosPet.append("pet ativo ");
            } else {
                dadosPet.append("pet inativo ");
            }

            holder.txtDadosPet.setText(dadosPet.toString());

        }

        return convertView;
    }


}
