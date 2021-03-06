package com.watabou.pixeldungeon.ui;

import android.util.Log;

import com.nyrds.android.util.FileSystem;
import com.nyrds.android.util.GuiProperties;
import com.nyrds.android.util.Unzip;
import com.nyrds.pixeldungeon.items.common.Library;
import com.nyrds.pixeldungeon.ml.BuildConfig;
import com.nyrds.pixeldungeon.ml.R;
import com.nyrds.pixeldungeon.support.PlayGames;
import com.watabou.noosa.Game;
import com.watabou.noosa.Text;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Rankings;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.windows.WndMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * Created by mike on 14.05.2017.
 * This file is part of Remixed Pixel Dungeon.
 */

class WndPlayGames extends Window {

	private int y = GAP;

	public WndPlayGames() {

		boolean playGamesConnected = PlayGames.isConnected();
		resizeLimited(120);

		Text listTitle = PixelScene.createMultiline(Game.getVar(R.string.WndPlayGames_Title), GuiProperties.mediumTitleFontSize());
		listTitle.hardlight(TITLE_COLOR);
		listTitle.maxWidth(width - GAP * 2);
		listTitle.measure();
		listTitle.x = (width - listTitle.width()) / 2;
		listTitle.y = y;

		add(listTitle);

		y += listTitle.height() + GAP;

		CheckBox usePlayGames = new CheckBox(Game.getVar(R.string.WndPlayGames_Use), playGamesConnected) {
			@Override
			public void checked(boolean value) {
				super.checked(value);

				if (value) {
					PlayGames.connect();
					Game.scene().add(new WndMessage(Game.getVar(R.string.WndPlayGames_Connecting)));
				} else {
					PlayGames.disconnect();
				}

				hide();
			}
		};

		addButton(usePlayGames);

		if (!playGamesConnected) {
			resize(width, y);
			return;
		}

		addButton(new RedButton(Game.getVar(R.string.WndPlayGames_Show_Badges)) {
			@Override
			protected void onClick() {
				super.onClick();
				PlayGames.showBadges();
			}
		});

		addButton(new RedButton(Game.getVar(R.string.WndPlayGames_Show_Leaderboards)) {
			@Override
			protected void onClick() {
				super.onClick();
				PlayGames.showLeaderboard();
			}
		});

		if(BuildConfig.DEBUG) {

			addButton(new RedButton("Zip Test") {
				@Override
				protected void onClick() {
					super.onClick();

					ByteArrayOutputStream out = new ByteArrayOutputStream();

					try {
						long t1 = System.nanoTime();
						FileSystem.zipFolderTo(out, FileSystem.getInternalStorageFile(""), 0, null);
						float size = out.toByteArray().length;
						long t2 = System.nanoTime();
						float time = (t2 - t1) / 1000000f;
						Log.i("zipped", String.format("size :%4.2f, time: %4.2f", size / 1024f, time));

						FileSystem.deleteRecursive(FileSystem.getInternalStorageFile(""));

						t1 = System.nanoTime();
						Unzip.unzip(new ByteArrayInputStream(out.toByteArray()),
								FileSystem.getInternalStorageFile("").getAbsolutePath());
						t2 = System.nanoTime();
						time = (t2 - t1) / 1000000f;
						Log.i("unzipped", String.format("size :%4.2f, time: %4.2f", size / 1024f, time));

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
		resize(width, y);
	}

	private void showActionResult(final boolean res) {
		Game.executeInGlThread(new Runnable() {
			@Override
			public void run() {
				if (res) {
					Game.scene().add(new WndMessage(Game.getVar(R.string.WndPlayGames_Show_Ok)));
				} else {
					Game.scene().add(new WndMessage(Game.getVar(R.string.WndPlayGames_Show_Error)));
				}
				hide();
			}
		});
	}

	private void addButton(TextButton btn) {
		btn.setRect(0, y, width, BUTTON_HEIGHT);
		add(btn);
		y += btn.height() + GAP;
	}
}
