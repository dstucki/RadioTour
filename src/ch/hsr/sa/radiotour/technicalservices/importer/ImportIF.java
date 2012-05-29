package ch.hsr.sa.radiotour.technicalservices.importer;

public interface ImportIF<T> {

	public T convertTo(String[] strings);
}
