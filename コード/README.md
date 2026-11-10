# ディズニー作品の鑑賞リスト管理

授業のComposite（スライド04）とState（スライド05）の適用前後を比較するJavaプログラムです。

## 実行方法

JDK 11以降を使用し、「コード」フォルダで実行してください。

```powershell
javac -encoding UTF-8 -d build before/BeforeMain.java after/AfterMain.java
java -cp build before.BeforeMain
java -cp build after.AfterMain
```

## 共通仕様

- リストの下にシリーズと作品を置き、表示と作品数の集計を行います。グループは任意の深さにできます。
- 作品は「未鑑賞 → 鑑賞中 → 鑑賞済み」と進み、鑑賞済みから再鑑賞すると鑑賞中へ戻ります。
- 集計は現在の状態だけを対象とします。再鑑賞中は鑑賞済み件数に含めません。履歴や鑑賞回数は保存しません。
- 不許可操作はメッセージだけを表示し、状態を変えません。
- 循環・共有・重複登録のない木を呼出し側で構成する前提です。これらの自動検出は未実装です。
- ファイル保存、検索、GUI、配信状況や上映時間の取得は対象外です。

## パターンの対応

適用前はEntryがgroupフラグで集合と作品を区別し、enumと条件分岐で状態を管理します。

適用後のCompositeはWatchEntryがComponent、WatchGroupがComposite、MovieがLeafです。表示と集計を共通の型へ委譲します。追加はWatchGroup、鑑賞操作はMovieだけが持ちます。

StateはMovieがContext、ViewingStateがState、Unwatched・Watching・WatchedがConcreteStateです。各作品は独立した状態を持ちます。

## 実行例の確認点

全12作品を「近年のアニメーション作品」8作品と「名作アニメーション作品」4作品に分け、近年の作品の下には2シリーズも置きます。初期・再鑑賞中は0/12、最終一覧は4/12です。最終状態は鑑賞済み4作品、鑑賞中2作品、未鑑賞6作品です。両分類と各シリーズの集計も確認できます。

## レポート

Overleaf/main.texは鑑賞リスト管理の本文・実行結果・コード全文に更新済みです。main.texとlatexmkrcをOverleafにアップロードし、CompilerをLaTeXに設定してください。
課題指定のPDFには前後のコード、本人の言葉での考察、参考資料、実際の生成AI利用履歴を掲載してください。本コードと説明は生成AIが作成しています。
