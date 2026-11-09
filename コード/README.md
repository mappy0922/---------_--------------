# 学園祭の模擬店注文管理

授業のDecoratorパターンとStateパターンについて、適用前後を比較するJavaプログラムです。
外部ライブラリ、画面操作、ネットワーク接続は不要です。
比較しやすくレポートに掲載しやすいよう、各版は1ファイルにまとめています。
内部のクラスは説明用の入れ子クラスであり、それぞれ独立した役割を持ちます。

## ファイル

- `before/BeforeMain.java`：商品種類・トッピング・状態を条件分岐で処理。
- `after/AfterMain.java`：DecoratorとStateを適用した同じ機能。

## 実行方法

JDK 11以降を使用し、このREADMEがある「コード」フォルダで実行します。

```powershell
javac -encoding UTF-8 -d build before/BeforeMain.java after/AfterMain.java
java -cp build before.BeforeMain
java -cp build after.AfterMain
```

両方のプログラムが同じ結果を出します。
注文1は焼きそば400円＋チーズ80円＋目玉焼き100円＝580円、
注文2はお好み焼き500円＋目玉焼き100円＝600円です。
調理前の受渡し、重複した調理開始、重複した受渡しは拒否されます。
どちらの注文も、最後は「受渡し済み」になります。

## 共通仕様

- 1注文につき基本商品1個。数量、支払い、在庫、取消しは扱いません。
- トッピングは注文生成時に確定します。実行例では各種類を1回ずつ指定します。
- 状態遷移は「受付済み → 調理中 → 受渡し待ち → 受渡し済み」の一方向です。
- 許可されない操作ではメッセージだけを表示し、状態は変えません。
- 適用後のDecoratorは重ねて使えるため、同一トッピングを重ねると追加分も課金されます。
  適用前のboolean表現は各トッピングの有無だけを表します。比較例は両方が扱える範囲です。

## パターンの対応と比較の観点

### Decorator（授業スライド04）

`Food`がComponent、`Yakisoba`と`Okonomiyaki`がConcreteComponent、
`Topping`がDecorator、`Cheese`と`FriedEgg`がConcreteDecoratorです。
基本商品をトッピングで包み、説明文と価格に情報を追加します。

適用前に新しいトッピングを追加するには、Orderのフィールド、コンストラクタ、
説明文生成、価格計算に修正が必要です。適用後はToppingのサブクラスを追加し、
注文を作る箇所で組み合わせます。既存商品の価格計算を変更せずに追加できます。
一方、クラス数が増え、包み込む構造を追う手間が生じます。

### State（授業スライド05）

`Order`がContext、`OrderState`がState、`Accepted`、`Cooking`、`Ready`、
`Delivered`がConcreteStateです。Orderが現在の状態に操作を委譲します。
不許可操作の共通処理はインタフェースのdefaultメソッドに置いています。

適用前は操作ごとに状態を判定します。適用後は各状態で可能な操作と次の状態を、
対応するクラスで確認できます。ただし、状態を追加する際には遷移元の修正も必要です。
状態が少なく規則が固定なら、適用前の方が短く理解しやすい場合もあります。

## 課題資料との関係

参照元は `授業スライド/out-class_report.pdf`、
`04_advanced_programming_structural_patterns.pdf`、
`05_advanced_programming_behavioral_patterns.pdf` です。
このコードとREADMEだけでは提出用レポートは完成しません。
課題では、前後のコードをレポート内に掲載し、利点・効果・必要性を
自分の言葉で説明したPDFを提出する必要があります。

生成AIで作成したコードです。課題の指示に従い、今回を含む実際の会話・利用履歴を
保存し、レポート末尾に添付してください。このREADMEは利用履歴の代わりにはなりません。
