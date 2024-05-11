package com.example.taskmanagementapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT )"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNote(note:Note) {
        val db = writableDatabase //Database can be Modified
        //Allows to take Arguments and Perform Operations
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        //Actually inserting the data into the Database

        db.insert(TABLE_NAME, null, values)

        //Closing Database Connection
        db.close()
    }

        fun getAllNotes(): List<Note>{

            //mutableList(modify or flexible)
            val notesList = mutableListOf<Note>()

            //Only need to read the Data
            val db = readableDatabase

            //SQL Query
            val query = "SELECT * FROM $TABLE_NAME"

            //Execute Query
            val cursor = db.rawQuery(query, null)

            //Iteration Purpose
            while (cursor.moveToNext()){
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

                val note = Note(id, title, content)
                notesList.add(note)
            }
            cursor.close()
            db.close()
            return notesList //Acts as a list consisting all data in the database
        }

    fun updateNote(note: Note){
        //need to edit the note, hence writable database

        val db = writableDatabase

        //Add values such as title and content into the respective columns

        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }

        //variable to identify the rows to be updated by its column ID
        val whereClause = "$COLUMN_ID = ?"
        //Initialize an Array containing the arguments note ID
        val whereArgs = arrayOf(note.id.toString())
        //Updating and closing the database connection
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getNoteByID(noteId: Int): Note{
        //Just reading the Database
        val db = readableDatabase

        //SQL Query to get all columns with the column ID = note ID

        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId "

        //Moves the Cursor to the first row of the result
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        //close the cursor and db
        cursor.close()
        db.close()
        //return the Note data class with ID TITLE and Content
        return Note(id, title, content)
    }

    fun deleteNote(noteId: Int){
        val db = writableDatabase
        val whereClaus = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClaus, whereArgs)
        db.close()
    }
}