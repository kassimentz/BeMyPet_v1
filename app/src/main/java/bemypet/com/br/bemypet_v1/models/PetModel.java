package bemypet.com.br.bemypet_v1.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bemypet.com.br.bemypet_v1.pojo.Filtros;
import bemypet.com.br.bemypet_v1.pojo.Pet;
import bemypet.com.br.bemypet_v1.pojo.PontoGeo;

/**
 * Created by kassianesmentz on 15/08/17.
 */

public class PetModel implements InterfaceModel<Pet> {

    private List<Pet> pets = new ArrayList<>();

    public List<Pet> getPets() {
        return pets;
    }

    public void addPets(Pet p) {
        this.pets.add(p);
    }

    @Override
    public void salvar(Pet entidade) {
        final Pet pet = entidade;
        FirebaseConnection.getConnection();
        DatabaseReference connectedReference = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    FirebaseConnection.getDatabase().child("pets").child(String.valueOf(pet.id)).setValue(pet);
                } else {
                    //logar erro
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Log.i("Cancel", "Listener was cancelled");
            }
        });
    }

    @Override
    public void listar(OnGetDataListener lis) {
        final OnGetDataListener listener = lis;
        listener.onStart();


        FirebaseDatabase.getInstance().getReference().child("pets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });

    }

    @Override
    public Pet procurarPorId(Integer id) {
        return null;
    }

    public List<Pet> getPetsDisponiveis() { return null; }

    public List<Pet> getPetsProximos(PontoGeo ponto) { return null; }

    public List<Pet> filtrarPets(Filtros filtros) { return null; }
}
