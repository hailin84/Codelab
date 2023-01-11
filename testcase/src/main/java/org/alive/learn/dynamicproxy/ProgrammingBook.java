package org.alive.learn.dynamicproxy;

public class ProgrammingBook implements IBook {

	public String showCatalog() {
		String catelog = "Programming";
		System.out.println("catelog=" + catelog);
		return catelog;
	}

	public String showLanguage() {
		String language = "English";
		System.out.println("language=" + language);
		return language;
	}

}
