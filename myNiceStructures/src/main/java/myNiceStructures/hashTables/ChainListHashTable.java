package myNiceStructures.hashTables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChainListHashTable<K, V> implements HashTable<K, V> {
	public static final int DEFAULT_TABLE_SIZE = 100;
	private int nbEntries = 0;
	
	private ArrayList<SimpleHashTableEntry<K, V>>[] table;

	@SuppressWarnings("unchecked")
	public ChainListHashTable() {
		this.table = new ArrayList[DEFAULT_TABLE_SIZE];
	}
	
	@SuppressWarnings("unchecked")
	public ChainListHashTable(int size) {
		this.table = new ArrayList[size];
	}
	
	@Override
	public Iterator<HashTableEntry<K, V>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int getIdxFromKey(K key) {
		if(key == null) {
			throw new NullPointerException("A key cannot be null.");
		}
		
		return key.hashCode() % this.table.length;
	}

	@Override
	public void put(K key, V value) {
		int idx = this.getIdxFromKey(key);
		
		// si pas de liste : créer la liste et y ajoute l'entrée
		if (this.table[idx] == null) {
			this.table[idx] = new ArrayList<>();
			this.table[idx].add(new SimpleHashTableEntry<>(key, value));
			this.nbEntries++;
		}
		
		else {
			// liste présente : cherche une entrée de même clé
			for (SimpleHashTableEntry<K, V> e : this.table[idx]) {
				if (e.getKey().equals(key)) {
					// entrée de même clé trouvée : met à jour la valeur
					e.setValue(value);
					return;
				}
			}
			
			// pas d'entrée de même clé : ajoute l'entrée à la liste
			this.table[idx].add(new SimpleHashTableEntry<>(key, value));
			this.nbEntries++;
		}
	}

	@Override
	public V remove(K key) {
		int idx = this.getIdxFromKey(key);
		
		// si pas de liste : retourne null
		if (this.table[idx] != null && !table[idx].isEmpty()) {
			Iterator<SimpleHashTableEntry<K, V>> itList = this.table[idx].iterator();
			while(itList.hasNext()) {
				SimpleHashTableEntry<K, V> e = itList.next();
				if (e.getKey().equals(key)) {
					// table[idx].remove(e);
					itList.remove();
					this.nbEntries--;
					/*
					if(table[idx].size() == 0) {
						table[idx] = null;
					}
					*/					
					return e.getValue();
				}
			}
		}
		
		return null;
	}

	@Override
	public V get(K key) {
		int idx = this.getIdxFromKey(key);
		
		if (this.table[idx] != null && !table[idx].isEmpty()) {
			for (SimpleHashTableEntry<K, V> e : this.table[idx]) {
				if (e.getKey().equals(key)) {
					return e.getValue();
				}
			}
		}
		
		return null;
	}

	@Override
	public boolean contains(K key) {
		int idx = this.getIdxFromKey(key);
		
		if (this.table[idx] != null && !table[idx].isEmpty()) {
			for (SimpleHashTableEntry<K, V> e : this.table[idx]) {
				if (e.getKey().equals(key)) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public int size() {
		return this.nbEntries;
	}

}
