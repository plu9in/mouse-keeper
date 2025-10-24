# Usage
    # Sans arg: maintenant -> 18:20
    java -jar app.jar

    # Fin explicite (plus flexible)
    java -jar app.jar "2025-10-24 22:30"
    java -jar app.jar "20:30"
    java -jar app.jar "now+90m"
    java -jar app.jar "1729792800"

    # Début + fin
    java -jar app.jar "2025-10-24 20:00" "2025-10-24 22:30"
    java -jar app.jar "20:00" "22:30"
    java -jar app.jar "now+10m" "now+2h"