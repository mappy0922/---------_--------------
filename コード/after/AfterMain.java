package after;

import java.util.ArrayList;
import java.util.List;

/** 適用後：Compositeで階層を、Stateで作品の鑑賞状態を管理する。 */
public class AfterMain {
    // Component：作品とグループに共通の操作。
    interface WatchEntry {
        int totalCount();
        int watchedCount();
        void print(String indent);
    }

    // Composite：子の種類を判定せず、表示と集計を再帰的に行う。
    static class WatchGroup implements WatchEntry {
        private final String title;
        private final List<WatchEntry> children = new ArrayList<>();

        WatchGroup(String title) { this.title = title; }
        void add(WatchEntry child) {
            children.add(java.util.Objects.requireNonNull(child));
        }
        public int totalCount() {
            int count = 0;
            for (WatchEntry child : children) count += child.totalCount();
            return count;
        }
        public int watchedCount() {
            int count = 0;
            for (WatchEntry child : children) count += child.watchedCount();
            return count;
        }
        public void print(String indent) {
            System.out.printf("%s[%s] %d/%d作品鑑賞済み%n",
                    indent, title, watchedCount(), totalCount());
            for (WatchEntry child : children) child.print(indent + "  ");
        }
    }

    // State：不許可操作の共通処理。許可する操作を各状態で上書きする。
    interface ViewingState {
        String label();
        default int watchedCount() { return 0; }
        default void start(Movie movie) { movie.reject("開始", label()); }
        default void finish(Movie movie) { movie.reject("完了", label()); }
        default void rewatch(Movie movie) { movie.reject("再鑑賞", label()); }
    }

    static class Unwatched implements ViewingState {
        public String label() { return "未鑑賞"; }
        public void start(Movie movie) {
            movie.changeState(new Watching());
            movie.message("鑑賞を開始しました");
        }
    }

    static class Watching implements ViewingState {
        public String label() { return "鑑賞中"; }
        public void finish(Movie movie) {
            movie.changeState(new Watched());
            movie.message("鑑賞を完了しました");
        }
    }

    static class Watched implements ViewingState {
        public String label() { return "鑑賞済み"; }
        public int watchedCount() { return 1; }
        public void rewatch(Movie movie) {
            movie.changeState(new Watching());
            movie.message("再鑑賞を開始しました");
        }
    }

    // CompositeのLeafであり、StateのContextでもある。
    static class Movie implements WatchEntry {
        private final String title;
        private ViewingState state = new Unwatched();

        Movie(String title) { this.title = title; }
        private void changeState(ViewingState next) { state = next; }
        private void message(String text) { System.out.println(title + "：" + text); }
        private void reject(String action, String label) {
            message(action + "不可（" + label + "）");
        }
        void start() { state.start(this); }
        void finish() { state.finish(this); }
        void rewatch() { state.rewatch(this); }
        public int totalCount() { return 1; }
        public int watchedCount() { return state.watchedCount(); }
        public void print(String indent) {
            System.out.println(indent + title + "：" + state.label());
        }
    }

    public static void main(String[] args) {
        WatchGroup root = new WatchGroup("ディズニー作品の鑑賞リスト");
        WatchGroup series = new WatchGroup("アナと雪の女王シリーズ");
        Movie first = new Movie("アナと雪の女王");
        Movie second = new Movie("アナと雪の女王2");
        Movie other = new Movie("塔の上のラプンツェル");
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
