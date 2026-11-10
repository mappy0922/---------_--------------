package before;

import java.util.ArrayList;
import java.util.List;

/** 適用前：種類フラグと状態の条件分岐によって鑑賞リストを管理する。 */
public class BeforeMain {
    enum Status { UNWATCHED, WATCHING, WATCHED }

    static class Entry {
        private final String title;
        private final boolean group;
        private final List<Entry> children = new ArrayList<>();
        private Status status = Status.UNWATCHED;

        Entry(String title, boolean group) {
            this.title = title;
            this.group = group;
        }

        void add(Entry child) {
            if (!group) throw new IllegalStateException("作品には追加できません");
            children.add(java.util.Objects.requireNonNull(child));
        }

        int totalCount() {
            if (!group) return 1;
            int count = 0;
            for (Entry child : children) count += child.totalCount();
            return count;
        }

        int watchedCount() {
            if (!group) return status == Status.WATCHED ? 1 : 0;
            int count = 0;
            for (Entry child : children) count += child.watchedCount();
            return count;
        }

        String label() {
            switch (status) {
                case UNWATCHED: return "未鑑賞";
                case WATCHING: return "鑑賞中";
                case WATCHED: return "鑑賞済み";
                default: throw new IllegalStateException();
            }
        }

        void print(String indent) {
            if (group) {
                System.out.printf("%s[%s] %d/%d作品鑑賞済み%n",
                        indent, title, watchedCount(), totalCount());
                for (Entry child : children) child.print(indent + "  ");
            } else {
                System.out.println(indent + title + "：" + label());
            }
        }

        void start() {
            requireMovie();
            if (status == Status.UNWATCHED) {
                status = Status.WATCHING;
                message("鑑賞を開始しました");
            } else reject("開始");
        }

        void finish() {
            requireMovie();
            if (status == Status.WATCHING) {
                status = Status.WATCHED;
                message("鑑賞を完了しました");
            } else reject("完了");
        }

        void rewatch() {
            requireMovie();
            if (status == Status.WATCHED) {
                status = Status.WATCHING;
                message("再鑑賞を開始しました");
            } else reject("再鑑賞");
        }

        private void requireMovie() {
            if (group) throw new IllegalStateException("作品を選択してください");
        }
        private void message(String text) { System.out.println(title + "：" + text); }
        private void reject(String action) { message(action + "不可（" + label() + "）"); }
    }

    public static void main(String[] args) {
        Entry root = new Entry("ディズニー作品の鑑賞リスト", true);
        Entry modern = new Entry("近年のアニメーション作品", true);
        Entry series = new Entry("アナと雪の女王シリーズ", true);
        Entry ralphSeries = new Entry("シュガー・ラッシュシリーズ", true);
        Entry classics = new Entry("名作アニメーション作品", true);
        Entry first = new Entry("アナと雪の女王", false);
        Entry second = new Entry("アナと雪の女王2", false);
        Entry ralph = new Entry("シュガー・ラッシュ", false);
        Entry ralph2 = new Entry("シュガー・ラッシュ：オンライン", false);
        Entry rapunzel = new Entry("塔の上のラプンツェル", false);
        Entry zootopia = new Entry("ズートピア", false);
        Entry baymax = new Entry("ベイマックス", false);
        Entry moana = new Entry("モアナと伝説の海", false);
        Entry lion = new Entry("ライオン・キング", false);
        Entry aladdin = new Entry("アラジン", false);
        Entry beauty = new Entry("美女と野獣", false);
        Entry mermaid = new Entry("リトル・マーメイド", false);
        root.add(modern);
        root.add(classics);
        modern.add(series);
        modern.add(ralphSeries);
        modern.add(rapunzel);
        modern.add(zootopia);
        modern.add(baymax);
        modern.add(moana);
        series.add(first);
        series.add(second);
        ralphSeries.add(ralph);
        ralphSeries.add(ralph2);
        classics.add(lion);
        classics.add(aladdin);
        classics.add(beauty);
        classics.add(mermaid);
        root.print("");
        System.out.println("--- 鑑賞操作 ---");
        first.finish();
        first.rewatch();
        first.start();
        first.start();
        first.finish();
        first.finish();
        first.start();
        first.rewatch();
        root.print("");
        first.finish();
        second.start();
        rapunzel.start();
        rapunzel.finish();
        ralph.start();
        ralph.finish();
        lion.start();
        lion.finish();
        zootopia.start();
        System.out.println("--- 最終一覧 ---");
        root.print("");
    }
}
