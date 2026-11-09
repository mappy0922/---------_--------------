package before;

/** デザインパターン適用前：トッピングと注文状態を条件分岐で処理する。 */
public class BeforeMain {
    enum Status { ACCEPTED, COOKING, READY, DELIVERED }

    static class Order {
        private final int number;
        private final String menu;
        private final boolean cheese;
        private final boolean egg;
        private Status status = Status.ACCEPTED;

        Order(int number, String menu, boolean cheese, boolean egg) {
            if (!menu.equals("焼きそば") && !menu.equals("お好み焼き")) {
                throw new IllegalArgumentException("未対応のメニュー: " + menu);
            }
            this.number = number;
            this.menu = menu;
            this.cheese = cheese;
            this.egg = egg;
        }

        String description() {
            String result = menu;
            if (cheese) result += " + チーズ";
            if (egg) result += " + 目玉焼き";
            return result;
        }

        int price() {
            int result = menu.equals("焼きそば") ? 400 : 500;
            if (cheese) result += 80;
            if (egg) result += 100;
            return result;
        }

        String statusLabel() {
            switch (status) {
                case ACCEPTED: return "受付済み";
                case COOKING: return "調理中";
                case READY: return "受渡し待ち";
                case DELIVERED: return "受渡し済み";
                default: throw new IllegalStateException();
            }
        }

        void startCooking() {
            if (status == Status.ACCEPTED) {
                status = Status.COOKING;
                System.out.println("調理を開始しました。");
            } else {
                System.out.println("調理開始不可（現在: " + statusLabel() + "）");
            }
        }

        void finishCooking() {
            if (status == Status.COOKING) {
                status = Status.READY;
                System.out.println("調理が完了しました。");
            } else {
                System.out.println("調理完了不可（現在: " + statusLabel() + "）");
            }
        }

        void deliver() {
            if (status == Status.READY) {
                status = Status.DELIVERED;
                System.out.println("商品を受け渡しました。");
            } else {
                System.out.println("受渡し不可（現在: " + statusLabel() + "）");
            }
        }

        void printSummary() {
            System.out.printf("注文%d: %s / %d円 / %s%n",
                    number, description(), price(), statusLabel());
        }
    }

    public static void main(String[] args) {
        Order first = new Order(1, "焼きそば", true, true);
        Order second = new Order(2, "お好み焼き", false, true);
        first.printSummary();
        second.printSummary();

        // 調理前の受渡し、調理開始の重複、受渡しの重複を拒否する。
        first.deliver();
        first.startCooking();
        first.startCooking();
        first.finishCooking();
        first.deliver();
        first.deliver();
        first.printSummary();

        second.startCooking();
        second.finishCooking();
        second.deliver();
        second.printSummary();
    }
}
