package database.controller;

import database.view.DatabaseFrame;

public class DatabaseAppController
{
	private DatabaseController database;
	private DatabaseFrame appFrame;

	public DatabaseAppController()
	{
		database = new DatabaseController(this);
		appFrame = new DatabaseFrame(this);
	}

	public void start()
	{

	}

	public DatabaseFrame getAppFrame()
	{
		return appFrame;
	}

	public DatabaseController getDatabase()
	{
		return database;
	}
}