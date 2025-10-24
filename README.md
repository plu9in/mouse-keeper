# Usage

    - Sans arg: maintenant -> 18:20
    ```
    java -jar mouse-keeper-all.jar
    ```

    - Fin explicite (plus flexible)
    ```
    java -jar mouse-keeper-all.jar "2025-10-24 22:30"
    java -jar mouse-keeper-all.jar "20:30"
    java -jar mouse-keeper-all.jar "now+90m"
    java -jar mouse-keeper-all.jar "1729792800"
    ```

    - Début + fin
    ```
    java -jar mouse-keeper-all.jar "2025-10-24 20:00" "2025-10-24 22:30"
    java -jar mouse-keeper-all.jar "20:00" "22:30"
    java -jar mouse-keeper-all.jar "now+10m" "now+2h"
    ```