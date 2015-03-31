package database.controller;


public class DatabaseRunner
{
	/**
	 * The Runner of the App. Runs the App
	 * @param args
	 */
	
	public static void main(String [] args)
	{
		DatabaseAppController appController = new DatabaseAppController();
		appController.start();
	}
}
