from csv import *

class Subscriber:
    def __init__(self, id, firstname, surname, subscription_paid, date_of_birth, title = None, address = None, town = None, country = None, postcode = None,  gender = None):
        self.id = int(id)
        self.firstname = firstname
        self.surname = surname
        self.subscription_paid = bool(subscription_paid)
        self.date_of_birth = date_of_birth
        self.title = title if title != "-" else None
        self.address = address if address != "-" else None
        self.town = town if town != "-" else None
        self.country = country if country != "-" else None
        self.postcode = postcode if postcode != "-" else None
        self.gender = gender if gender != "-" else None
    
    @staticmethod
    def load_database(path):
        with open(path) as file:
            csv_r = reader(file, delimiter=",")
            res = []
            counter = 0
            for r in csv_r:
                row = [x.strip() for x in r]
                if counter > 0 : 
                    res.append(Subscriber(row[0], row[1], row[2], row[8], row[10], row[3], row[4], row[5], row[6], row[7], row[9]))
                counter += 1

        return res

    @staticmethod
    def payment_from_GB(subs):
        for s in [x for x in subs if x.subscription_paid and x.country == "gb"]:
            print(str(s.id) + " " + s.firstname + " " + s.surname)

if __name__ == "__main__":
    res = Subscriber.load_database("data/people.csv")
    Subscriber.payment_from_GB(res)