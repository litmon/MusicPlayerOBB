# Music Playing from OBB file

OBBファイル(Expansion Files)を使った音楽再生テスト
https://developer.android.com/google/play/expansion-files

## Prepare

- emulatorを作成し起動
- Android Studio上で View > Tool Windows > Device File Explorer を開く
- `/sdcard/Android/obb/com.litmon.app.musicplayer/` の下にリポジトリの `obb/main.1.com.litmon.app.musicplayer.sample.obb` を配置
  - フォルダを右クリックしてUploadを選択するとファイル選択ダイアログが表示される

## Usage

- アプリを起動して、ログに以下の表示が出ていればOK
  - `D/MainActivity: obbDir /storage/emulated/0/Android/obb/com.litmon.app.musicplayer`
- 再生ボタンを押す
  - 音楽が再生される
