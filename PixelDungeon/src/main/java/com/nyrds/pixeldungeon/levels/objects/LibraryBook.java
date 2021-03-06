package com.nyrds.pixeldungeon.levels.objects;

import com.nyrds.pixeldungeon.ml.R;
import com.nyrds.pixeldungeon.windows.WndLibrary;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mike on 01.07.2016.
 */
public class LibraryBook extends LevelObject {

	public LibraryBook(){
		super(-1);
	}

	public LibraryBook(int pos) {
		super(pos);
	}

	@Override
	void setupFromJson(Level level, JSONObject obj) throws JSONException {
	}

	@Override
	public boolean interact(Hero hero) {
		GameScene.show(new WndLibrary());

		return false;
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
	}

	@Override
	public void burn() {
	}

	@Override
	public boolean stepOn(Char hero) {
		return false;
	}

	@Override
	public String desc() {
		return Game.getVar(R.string.LibraryBook_Description);
	}

	@Override
	public String name() {
		return Game.getVar(R.string.LibraryBook_Name);
	}

	@Override
	public int image() {
		return 1;
	}
}
