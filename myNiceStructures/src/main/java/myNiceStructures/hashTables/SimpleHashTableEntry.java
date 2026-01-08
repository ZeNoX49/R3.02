package myNiceStructures.hashTables;

public class SimpleHashTableEntry<K, V> implements HashTableEntry<K, V> {
	private final K key;
	private V value;

	public SimpleHashTableEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return this.key;
	}

	@Override
	public V getValue() {
		return this.value;
	}
	
	public void setValue(V value) {
		this.value = value;
	}

}
