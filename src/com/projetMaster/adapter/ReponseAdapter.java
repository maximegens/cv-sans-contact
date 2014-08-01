package com.projetMaster.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.projetMaster.activity.R;
import com.projetMaster.recherche.Reponse;

/**
 * Classe ReponseAdapter - Cette classe gére le bon fonctionnement de la listView de la classe "VoirMesReponse".
 *
 */
public class ReponseAdapter extends BaseAdapter {

	List<Reponse> lesReponses;
	LayoutInflater inflater;
	
	public ReponseAdapter(Context context,List<Reponse> lesReponses) {
		this.inflater = LayoutInflater.from(context);
		this.lesReponses = lesReponses;
	}
	
	/**
	 * Donnes le nombre d’éléments dans la liste.
	 * @return Le nombre d’éléments dans la liste.
	 */
	@Override
	public int getCount() {
		return this.lesReponses.size();
	}

	/**
	 * Donnes l'objet Recherche à la position indiquée.
	 * @return L'objet Recherche à la position indiquée.
	 */
	@Override
	public Object getItem(int position) {
		return this.lesReponses.get(position);
	}

	/**
	 * Donnes l’id du la recherche.
	 * @return L'id de la recherche.
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 
	 * classe privé permettant de mémoriser les éléments 
	 * de la liste afin d'éviter le scintillement de l'écran lors des rafraichissement
	 *
	 */
	private class ViewHolder{
		TextView itemNumReponse;
		TextView itemResumeReponse;

	}
	
	/**
	 * Donnes la vue de l’item pour l’affichage.
	 * @return la vue de l’item pour l’affichage.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView == null){
			
			holder = new ViewHolder();
			convertView = this.inflater.inflate(R.layout.items_reponse, null);
			holder.itemNumReponse = (TextView)convertView.findViewById(R.id.itemNumReponse);
			holder.itemResumeReponse = (TextView)convertView.findViewById(R.id.itemResumeReponse);

			convertView.setTag(holder);
		}else 
			holder = (ViewHolder) convertView.getTag();
			
		holder.itemNumReponse.setText("Voir la réponse n°"+position);
		holder.itemResumeReponse.setText("Résumé : "+lesReponses.get(position).getResume());

		return convertView;
		
	}
}
