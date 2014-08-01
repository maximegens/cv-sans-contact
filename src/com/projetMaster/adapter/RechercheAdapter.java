package com.projetMaster.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.projetMaster.activity.R;
import com.projetMaster.recherche.Recherche;

/**
 * Classe RechercheAdapter - Cette classe g�re le bon fonctionnement de la listView de la classe "VoirMesRecherches".
 *
 */
public class RechercheAdapter extends BaseAdapter {

	List<Recherche> lesRecherches;
	LayoutInflater inflater;
	
	public RechercheAdapter(Context context,List<Recherche> lesRecherches) {
		this.inflater = LayoutInflater.from(context);
		this.lesRecherches = lesRecherches;
	}
	
	/**
	 * Donnes le nombre d��l�ments dans la liste.
	 * @return Le nombre d��l�ments dans la liste.
	 */
	@Override
	public int getCount() {
		return this.lesRecherches.size();
	}

	/**
	 * Donnes l'objet Recherche � la position indiqu�e.
	 * @return L'objet Recherche � la position indiqu�e.
	 */
	@Override
	public Object getItem(int position) {
		return this.lesRecherches.get(position);
	}

	/**
	 * Donnes l�id du la recherche.
	 * @return L'id de la recherche.
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 
	 * classe priv� permettant de m�moriser les �l�ments 
	 * de la liste afin d'�viter le scintillement de l'�cran lors des rafraichissement
	 *
	 */
	private class ViewHolder{
		TextView itemType;
		TextView itemCategorie;
		TextView itemPoste;
		TextView itemReponse;
	}
	
	/**
	 * Donnes la vue de l�item pour l�affichage.
	 * @return la vue de l�item pour l�affichage.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView == null){
			
			holder = new ViewHolder();
			convertView = this.inflater.inflate(R.layout.items_recherches, null);
			holder.itemType = (TextView)convertView.findViewById(R.id.itemType);
			holder.itemCategorie = (TextView)convertView.findViewById(R.id.itemCategorie);
			holder.itemPoste = (TextView)convertView.findViewById(R.id.itemPoste);
			holder.itemReponse = (TextView)convertView.findViewById(R.id.itemReponse);
			convertView.setTag(holder);
		}else 
			holder = (ViewHolder) convertView.getTag();
			
		holder.itemType.setText("Type : "+lesRecherches.get(position).getType());
		holder.itemCategorie.setText("Categorie : "+lesRecherches.get(position).getCategories());
		holder.itemPoste.setText("Poste : "+lesRecherches.get(position).getPoste());
		holder.itemReponse.setText("Aucunes r�ponses");
		return convertView;
		
	}
}
