package com.hp.client.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBSQLite extends SQLiteOpenHelper{

	private final static String TAG = "DBSQLite";
	
	private static volatile DBSQLite instance= null;
	
	/** Declaration des tables et de leurs colonnes*/
	
	private static final String TABLE_COMMENTAIRE = "COMMENTAIRE";
	private static final String COL_ID = "ID";
	private static final String COL_COMMENTAIRETEXT = "COMMENTAIRETEXT";
	private static final String COL_NBLIKE = "NBLIKE";
	private static final String COL_FKPRODUCT = "FKPRODUCT";
	
	private static final String TABLE_INGREDIENT = "INGREDIENT";
	private static final String COL_NOM = "NOM";
	private static final String COL_TYPEINGREDIENT = "TYPEINGREDIENT";
	private static final String COL_INFOS = "INFOS";
	private static final String COL_QUALITE= "QUALITE";

	private static final String TABLE_LABEL = "LABEL";
	private static final String COL_SIGLE= "SIGLE";
	private static final String COL_IMAGELABEL= "IMAGELABEL";
  
	private static final String TABLE_PRODUCT = "PRODUCT";
	private static final String COL_EAN = "EAN";
	private static final String COL_PRIXMOYEN = "PRIXMOYEN";
	private static final String COL_KMPARCOURU = "KMPARCOURU";
	private static final String COL_KCAL = "KCAL";
	private static final String COL_QTEGLUCIDE = "QTEGLUCIDE";
	private static final String COL_QTELIPIDE = "QTELIPIDE";

	private static final String TABLE_PRODUCTINGREDIENT = "PRODUCTINGREGIENT";
	private static final String COL_QUANTITE = "QUANTITE";
	private static final String COL_FKINGREDIENT = "FKINGREDIENT";
	
	private static final String TABLE_PRODUCTLABEL = "PRODUCTLABEL";
	private static final String COL_FKLABEL = "FKLABEL";
	
	private static final String TABLE_USER = "USER";
	private static final String COL_PRENOM = "PRENOM";
	private static final String COL_TYPECONSO = "TYPECONSO";
	
	private static final String TABLE_USERINGREDIENT = "USERINGREDIENT";
	private static final String COL_FKUSER = "FKUSER";
	
	private static final String TABLE_HISTORIQUE = "HISTORIQUE";
	
	
 /** SQL de creation des tables */
	
	private static final String CREATE_USER = "CREATE TABLE "+ TABLE_USER + " ("+
			COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_TYPECONSO + " INTEGER NOT NULL, " +
			COL_PRENOM + " VARCHAR(20) NOT NULL ) ";
	
	private static final String CREATE_USERINGREDIENT = " CREATE TABLE "+ TABLE_USERINGREDIENT + " ("+
			COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			COL_FKUSER + " INTEGER NOT NULL, " +
			COL_FKINGREDIENT + " INTEGER NOT NULL," +
			"FOREIGN KEY ("+COL_FKINGREDIENT+") REFERENCES "+TABLE_INGREDIENT+" ("+COL_ID+")," +
			"FOREIGN KEY ("+COL_FKUSER+") REFERENCES "+TABLE_USER+" ("+COL_ID+")" +
					") ";
	
	private static final String CREATE_PRODUCTINGREDIENT = "CREATE TABLE "+ TABLE_PRODUCTINGREDIENT+"(" +
			COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			COL_FKPRODUCT + " INTEGER NOT NULL," +
			COL_QUANTITE + " VARCHAR(20) NULL," +
			COL_FKINGREDIENT + " INTEGER NOT NULL," +
			"FOREIGN KEY ("+COL_FKINGREDIENT+") REFERENCES "+TABLE_INGREDIENT+" ("+COL_ID+")," +
			"FOREIGN KEY ("+COL_FKPRODUCT+") REFERENCES "+TABLE_PRODUCT+" ("+COL_ID+")" +
					") ";
	
	private static final String CREATE_PRODUCTLABEL = " CREATE TABLE "+TABLE_PRODUCTLABEL+" (" +
			COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			COL_FKPRODUCT+" INTEGER NOT NULL, " +
			COL_FKLABEL+" INTEGER NOT NULL," +
			"FOREIGN KEY ("+COL_FKPRODUCT+") REFERENCES "+TABLE_PRODUCT+" ("+COL_ID+")," +
			"FOREIGN KEY ("+COL_FKLABEL+") REFERENCES "+TABLE_LABEL+" ("+COL_ID+")" +
					")";
	
	private static final String CREATE_COMMENTAIRES = "CREATE TABLE "+ TABLE_COMMENTAIRE+" ( " +
			COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_COMMENTAIRETEXT+ " VARCHAR(255) NOT NULL, " +
			COL_NBLIKE+ " INTEGER DEFAULT 0 NOT NULL, " +
			COL_FKPRODUCT+" INTEGER NOT NULL," +
			COL_PRENOM+" VARCHAR(25) NOT NULL," +
			"FOREIGN KEY ("+COL_FKPRODUCT+") REFERENCES "+TABLE_PRODUCT+" ("+COL_ID+")"+
			") ";
	
	private static final String CREATE_INGREDIENT = "CREATE TABLE "+TABLE_INGREDIENT+" (" +
			COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
			COL_NOM + " VARCHAR(255) NOT NULL," +
			COL_TYPEINGREDIENT +" VARCHAR(255) NULL," +
			COL_INFOS +	" VARCHAR(255) NULL," +
			COL_QUALITE + " INTEGER NULL" +
					") ";
	
	private static final String CREATE_LABEL = "CREATE TABLE "+TABLE_LABEL+" (" +
			COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			COL_NOM + " VARCHAR(255) NOT NULL," +
			COL_SIGLE +	" VARCHAR(15) NULL," +
			COL_IMAGELABEL+" VARCHAR(255) NULL" +
					"	) ";
	
	private static final String CREATE_PRODUCT = "CREATE TABLE "+ TABLE_PRODUCT +"(" +
			COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
			COL_NOM + " VARCHAR(255) NOT NULL," +
			COL_EAN +" VARCHAR(25) UNIQUE NOT NULL," +
			COL_PRIXMOYEN +	" FLOAT NULL," +
			COL_KMPARCOURU + " INTEGER NULL," +
			COL_KCAL + " INTEGER NULL," +
			COL_QTEGLUCIDE + " FLOAT NULL," +
			COL_QTELIPIDE + " FLOAT NULL" +
					") ";
	
	
	private static final String CREATE_HISTORIQUE = "CREATE TABLE "+ TABLE_HISTORIQUE+"(" +
			COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			COL_FKPRODUCT + " INTEGER NOT NULL," +
			"FOREIGN KEY ("+COL_FKPRODUCT+") REFERENCES "+TABLE_PRODUCT+" ("+COL_ID+")" +
					") ";

//	private static final String CREATE_DB = CREATE_PRODUCTINGREDIENT+
//			CREATE_PRODUCTLABEL+CREATE_COMMENTAIRES+
//			CREATE_INGREDIENT+CREATE_LABEL+
//			CREATE_PRODUCT;
	
	private DBSQLite(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
 
	static public DBSQLite getInstance(Context context, String name, CursorFactory factory, int version){
		if (DBSQLite.instance == null) {
            synchronized(DBSQLite.class) {
              if (DBSQLite.instance == null) {
            	  Log.i(TAG, "getInstance");
            	  DBSQLite.instance = new DBSQLite(context,name,factory,version);
              }
            }
         }
         return DBSQLite.instance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

		 db.beginTransaction();
		   try {
			   db.execSQL(CREATE_INGREDIENT);
			   db.execSQL(CREATE_PRODUCT);
			   db.execSQL(CREATE_LABEL);
			   db.execSQL(CREATE_USER);
			   db.execSQL(CREATE_COMMENTAIRES);
			   db.execSQL(CREATE_PRODUCTLABEL);
			   db.execSQL(CREATE_PRODUCTINGREDIENT);
			   db.execSQL(CREATE_USERINGREDIENT);
			   db.execSQL(CREATE_HISTORIQUE);
		     db.setTransactionSuccessful();
		   } finally {
		     db.endTransaction();
		   }

		// initialisation de la bdd avec des donnees :
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('sucre','composant',3)");//1
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('huile de palme','composant',1)");//2
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('noisette','composant',4)");//3
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('cacao','composant',4)");//4
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('lait ecreme en poudre','composant',3)");//5
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('lecithines','emulsifiant',2)");//6
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('vanilline','emulsifiant',2)");//7
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('huile de tournesol','composant',4)");//8
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('cereales','composant',5)");//9
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('matiere grasse vegetale','composant',2)");//10
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('sirop de glucose','composant',3)");//11
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('amidon de ble','composant',3)");//12
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('lactose','composant',4)");//13
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('lait entier','composant',4)");//14
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('sel','composant',3)");//15
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('E476','emulsifiant',2)");//16
		
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('jus de cerise','composant',5)");//17
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('farine de ble','composant',4)");//18
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('fructose','composant',3)");//19
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('Beurre de cacao','composant',3)");//20
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('oeuf','composant',4)");//21
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('graisse vegetale','composant',2)");//22
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('E471','emulsifiant',2)");//23
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('amidon de ble','stabilisant',3)");//24
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('sirop de sorbitol','stabilisant',1)");//25

		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('lecithine de soja','emulsifiant',2)");//26
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('carbonate acide d ammonium','composant',1)");//27
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('carbonate acide de sodium','composant',1)");//28
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('citrates de sodium','composant',1)");//29
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('amidon de tapioca','composant',2)");//30
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('viande','composant',4)");//31
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('porc','composant',4)");//32
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('gluten','composant',3)");//33
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('aspartame','edulcorant',1)");//34
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('sesame','composant',5)");//35
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('huile d olive','composant',5)");//36
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('E220','edulcorant',2)");//37
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('eau gazifié','composant',4)");//38
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('acesulfame-K ','composant',1)");//39
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('cafeine','composant',3)");//40
		
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('eau','composant',5)");//41
		db.execSQL("INSERT INTO INGREDIENT(NOM,TYPEINGREDIENT, QUALITE) VALUES ('semoule de ble','composant',4)");//42


		db.execSQL("INSERT INTO PRODUCT(NOM,EAN, PRIXMOYEN, KMPARCOURU, KCAL, QTEGLUCIDE, QTELIPIDE)" +
				" VALUES ('Nutella 440g','nutella',2.86,120,544,57.3,31.6)");//1
		db.execSQL("INSERT INTO PRODUCT(NOM,EAN, PRIXMOYEN, KMPARCOURU, KCAL, QTEGLUCIDE, QTELIPIDE)" +
				" VALUES ('Petales de ble au Chocolat','petaledeble',2.03,209,405,77,5.5)");//2
		db.execSQL("INSERT INTO PRODUCT(NOM,EAN, PRIXMOYEN, KMPARCOURU, KCAL, QTEGLUCIDE, QTELIPIDE)" +
				" VALUES ('Prince multi-cereales, gout chocolat','prince',2.53,53,460,69,17)");//3
		db.execSQL("INSERT INTO PRODUCT(NOM,EAN, PRIXMOYEN, KMPARCOURU, KCAL, QTEGLUCIDE, QTELIPIDE)" +
				" VALUES ('Biscuits fourres a la cerise','cerise',0.90,560,384,69,10)");//4		
		db.execSQL("INSERT INTO PRODUCT(NOM,EAN, PRIXMOYEN, KMPARCOURU, KCAL, QTEGLUCIDE, QTELIPIDE)" +
				" VALUES ('Pate fraiche','patefraiche',1.20,160,266,54,2)");//5
		db.execSQL("INSERT INTO PRODUCT(NOM,EAN, PRIXMOYEN, KMPARCOURU, KCAL, QTEGLUCIDE, QTELIPIDE)" +
				" VALUES ('Coca cola light','cocacolalight',0.45,160,105,27,0)");//6
		
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (1,1)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (1,2)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT,QUANTITE) VALUES (1,3,'13%')");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT,QUANTITE) VALUES (1,4,'7,4%')");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT,QUANTITE) VALUES (1,5,'6,6%')");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (1,6)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (1,7)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (2,1)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (2,8)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (2,4)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (3,1)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (3,4)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (3,6)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (3,9)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (3,10)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (3,11)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (3,12)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (3,13)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (3,14)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (3,15)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (3,16)");
		
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,17)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,18)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,19)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,20)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,21)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,22)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,23)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,24)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,25)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,26)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,27)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,28)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,29)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (4,30)");
		
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (6,38)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (6,39)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (6,40)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (6,34)");
		
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (5,41)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (5,21)");
		db.execSQL("INSERT INTO PRODUCTINGREGIENT(FKPRODUCT,FKINGREDIENT) VALUES (5,42)");

		db.execSQL("INSERT INTO COMMENTAIRE(COMMENTAIRETEXT,FKPRODUCT,PRENOM) VALUES ('le nutella oui oui cest bon mais ca fait grossir :( ',1,'Laurence')");
		db.execSQL("INSERT INTO COMMENTAIRE(COMMENTAIRETEXT,FKPRODUCT,PRENOM) VALUES ('jaime le nutella ',1,'Clement')");
		db.execSQL("INSERT INTO COMMENTAIRE(COMMENTAIRETEXT,FKPRODUCT,PRENOM) VALUES ('il sont degueux ces princes ',3,'Max')");
		db.execSQL("INSERT INTO COMMENTAIRE(COMMENTAIRETEXT,FKPRODUCT,PRENOM) VALUES ('bons mais pas tres sains :/',4,'Alex95')");
		db.execSQL("INSERT INTO COMMENTAIRE(COMMENTAIRETEXT,FKPRODUCT,PRENOM) VALUES ('bien d accord avec toi ',4,'Vladimir')");

		
		db.execSQL("INSERT INTO LABEL(NOM,SIGLE) VALUES ('Agriculture Biologique UE','AB')");
		db.execSQL("INSERT INTO LABEL(NOM,SIGLE) VALUES ('Fair Trade','FT')");
		db.execSQL("INSERT INTO LABEL(NOM,SIGLE) VALUES ('Agriculture Biologique FR','AB')");
		
		db.execSQL("INSERT INTO PRODUCTLABEL(FKPRODUCT,FKLABEL) VALUES (3,1)");
		db.execSQL("INSERT INTO PRODUCTLABEL(FKPRODUCT,FKLABEL) VALUES (2,2)");
		db.execSQL("INSERT INTO PRODUCTLABEL(FKPRODUCT,FKLABEL) VALUES (5,1)");
		db.execSQL("INSERT INTO PRODUCTLABEL(FKPRODUCT,FKLABEL) VALUES (5,3)");

	
	}

 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_COMMENTAIRE + ";");
		db.execSQL("DROP TABLE " + TABLE_INGREDIENT + ";");
		db.execSQL("DROP TABLE " + TABLE_LABEL + ";");
		db.execSQL("DROP TABLE " + TABLE_PRODUCT + ";");
		db.execSQL("DROP TABLE " + TABLE_PRODUCTINGREDIENT + ";");
		db.execSQL("DROP TABLE " + TABLE_PRODUCTLABEL + ";");
		db.execSQL("DROP TABLE " + TABLE_HISTORIQUE + ";");
		
		onCreate(db);
	}
	
}
