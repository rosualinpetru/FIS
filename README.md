# ToM

### Descriere generală
ToM   este   o  aplicație   WEB   de   gestiune   a   zilelor   de   concediu   ale  angajaților   tuturor departamentelor unei firme, fiind folosită strict pentru uz intern.Ea este administrată de departamentul de IT al firmei, care asigurămentenațași înregistrarea de conturi de autentificare ale angajaților, solicitate de departamentul de HR.

###  Roluri la autentificare
* Angajat (pentru care se cunoaște liderul de echipăși departamenul în care lucrează)
* Reprezentant HR
* Reprezentant IT

### Reprezentant HR
După procesarea datelor unui angajat, prin intermediul aplicației, echipa de HR îi va solicita echipei de IT să creeze un cont pentru angajatul respectiv.Toate  solicitările  de  concediu  ale  angajaților,  înaintate  de  liderii  de  echipă  ai  firmei,  vor  fi procesate  de  către  departamentul  de  HR.  Anagajatul  va  primi  ulterior  datele  necesare  pentru concediu, tot prin intermediul aplicației.De  precizat  este  faptul că și reprezentanții  HR  dispun  de funcționalitățile aplicației  la  fel  ca  un angajat.

### Reprezentant IT
Scopul acestui rol este de a crea noi conturi (pe baza datelor personale primite de la HR) pentru angajați, de a le trimite credențialele pe email-ul de lucru și de a se ocupa de mentenanța platformei. Parola se va genera pe loc, va fi transmisă angajatului, care are obligația de a-și alege o noua parolă la prima autentificare. De  precizat  este  faptul că și reprezentanții  IT  dispun  de funcționalitățile aplicației  la  fel  ca  un angajat (Deși au rolul de mentenanță, nu au drept de decizie pentru validarea concediilor) .


### Angajat
Când un angajat primește credențialele contului, acesta va fi obligat să își seteze o nouă parolă. Fiecare   angajat   poate   solicita   prin   intermediul  aplicației   tipul   de   concediu   (medical,   de maternitate, situații  familiale  speciale,  home-office, odihnă)  dorite  pe  o anumităperioadă.  Cererea este înaintată  liderului  de echipă,  care  o  poate  accepta  sau  nu  (în  ambele  cazuri,  solicitantul  este notificat), printr-o interfață grafică dedicată. Dacă solicitantul este un lider de echipă, trebuie delegat un  nou  lider,  deci  solicitarea  trebuie aprobată  de către  liderul său  de echipă  („liderul  liderului”) și acceptată   de   persoana  delegată. Dacă   o   solicitare   este  acceptată,   aceasta   va   fi  trimisă departamentului de HR.


### Tehnologii
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### Cursuri
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
