package after;

/** デザインパターン適用後：Decoratorで商品を構成し、Stateで注文を管理する。 */
public class AfterMain {
    // Component：基本商品とトッピングを共通の型で扱う。
    interface Food {
        String description();
        int price();
    }

    static class Yakisoba implements Food {
        public String description() { return "焼きそば"; }
        public int price() { return 400; }
    }

    static class Okonomiyaki implements Food {
        public String description() { return "お好み焼き"; }
        public int price() { return 500; }
    }

    // Decorator：包み込んだ商品に処理を委譲する。
    abstract static class Topping implements Food {
        protected final Food food;

        Topping(Food food) {
            this.food = java.util.Objects.requireNonNull(food);
        }

        public String description() { return food.description(); }
        public int price() { return food.price(); }
    }

    static class Cheese extends Topping {
        Cheese(Food food) { super(food); }
        public String description() { return super.description() + " + チーズ"; }
        public int price() { return super.price() + 80; }
    }

    static class FriedEgg extends Topping {
        FriedEgg(Food food) { super(food); }
        public String description() { return super.description() + " + 目玉焼き"; }
        public int price() { return super.price() + 100; }
    }

    // State：各状態で許可する操作だけを具象状態クラスで実装する。
    interface OrderState {
        String label();

        default void startCooking(Order order) {
            System.out.println("調理開始不可（現在: " + label() + "）");
        }

        default void finishCooking(Order order) {
            System.out.println("調理完了不可（現在: " + label() + "）");
        }

        default void deliver(Order order) {
            System.out.println("受渡し不可（現在: " + label() + "）");
        }
    }

    static class Accepted implements OrderState {
        public String label() { return "受付済み"; }
        public void startCooking(Order order) {
            order.changeState(new Cooking());
            System.out.println("調理を開始しました。");
        }
    }

    static class Cooking implements OrderState {
        public String label() { return "調理中"; }
        public void finishCooking(Order order) {
            order.changeState(new Ready());
            System.out.println("調理が完了しました。");
        }
    }

    static class Ready implements OrderState {
        public String label() { return "受渡し待ち"; }
        public void deliver(Order order) {
            order.changeState(new Delivered());
            System.out.println("商品を受け渡しました。");
        }
    }

    static class Delivered implements OrderState {
        public String label() { return "受渡し済み"; }
    }

    // Context：状態ごとの分岐を書かず、現在の状態オブジェクトへ委譲する。
    static class Order {
        private final int number;
        private final Food food;
        private OrderState state = new Accepted();

        Order(int number, Food food) {
            this.number = number;
            this.food = java.util.Objects.requireNonNull(food);
        }

        private void changeState(OrderState next) { state = next; }
        void startCooking() { state.startCooking(this); }
        void finishCooking() { state.finishCooking(this); }
        void deliver() { state.deliver(this); }

        void printSummary() {
            System.out.printf("注文%d: %s / %d円 / %s%n",
                    number, food.description(), food.price(), state.label());
        }
    }

    public static void main(String[] args) {
        Order first = new Order(1, new FriedEgg(new Cheese(new Yakisoba())));
        Order second = new Order(2, new FriedEgg(new Okonomiyaki()));
        first.printSummary();
        second.printSummary();

        // 適用前と同じ操作を、同じ順序で実行する。
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
