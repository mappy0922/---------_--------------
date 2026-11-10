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
        Entry series = new Entry("アナと雪の女王シリーズ", true);
        Entry first = new Entry("アナと雪の女王", false);
        Entry second = new Entry("アナと雪の女王2", false);
        Entry other = new Entry("塔の上のラプンツェル", false);
        root.add(series);
        series.add(first);
        series.add(second);
        root.add(other);
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
        System.out.println("--- 最終一覧 ---");
        root.print("");
    }
}
