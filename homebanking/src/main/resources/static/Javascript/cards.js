let credit_cards = new Vue({
  el: '#credit_cards',
  data: {
    cuentas: [],
    clients: [],
    cards: [],
    debitCards: [],
    creditCards: [],
    tarjetasActivas:[]
  },

  created() {
    //Llamo al metodo loadData() para cargar la informacion que necesito mostrar en la pagina
    this.loadData();
    this.loadClients();
  },

  methods: {

    // Hago la llamada a la API que me devuelva la informacion del cliente autenticado en este caso, para poder acceder alli a las tarjetas correspondientes
    loadData() {
      axios.get('api/clients/current')
        .then(function (response) {
          //Llamo a la Api y guardo todas las tarjetas de credito en una lista, y las cuentas en otra lista
          console.log(response);
          credit_cards.cuentas = response.data.accounts;
          credit_cards.cards = response.data.cards;
          //Inicializo el array Credit Cards con las credits cards correspondientes
          credit_cards.obtainsCreditCards();
          //Inicializo el array Debit Cards con las debits cards correspondientes
          credit_cards.obtainsDebitCards();
          console.log(credit_cards.cards);
          credit_cards.cardsActivas();
        })
        .catch(function (error) {
          // handle error
          console.log(error);
        })
        .then(function () {
          // always executed
        });
    },
    cardsActivas(){
      credit_cards.tarjetasActivas = credit_cards.cards.filter(card=>card.esActiva);      
    },
    //Metodo para hacer mayusculas
    toUpper(word) {
      return word.toUpperCase();
    },
    // Metodo para recortar la fecha
    cut(date, inicio, largo) {
      return date.toString().substr(inicio, largo);
    },
    obtainsDebitCards() {
      //Metodo que arma un arreglo con objetos de las tarjetas de debito
      credit_cards.cards.forEach(element => {
        if (element.cardType == "DEBIT") {
          credit_cards.debitCards.push(element);
        }
      });
    },
    obtainsCreditCards() {
      //Metodo que arma un arreglo con objetos de las tarjetas de credito
      credit_cards.cards.forEach(element => {
        if (element.cardType == "CREDIT") {
          credit_cards.creditCards.push(element);
        }
      });
    },
    loadClients() {
      axios.get('./rest/clients')
        .then(function (response) {
          //Llamo a la Api y guardo todas las tarjetas de credito en una lista, y las cuentas en otra lista
          console.log(response);
          credit_cards.clients = response.data._embedded.clients;

        })
        .catch(function (error) {
          // handle error
          console.log(error);
        })
        .then(function () {
          // always executed
        });
    },
    eliminarTarjeta(number) {
      console.log(number);
      Swal.fire({
        title: 'Estas seguro/a que quiere dar de baja la tarjeta?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3EBD02',
        cancelButtonColor: '#d33',
        cancelButtonText: 'Cancelar',
        confirmButtonText: 'Si, Quiero dar de baja la tarjeta!'
      }).then((result) => {
        console.log("ENTRO AL AXIOS");
        axios.patch('api/clients/delete/card',"cardNumber="+number)
          .then(response=>{
            console.log("Tarjeta eliminada con éxito");
            this.loadData();
            location.reload()
          }            
          )
          .catch(function (error) {
            // handle error
            console.log(error);
          })
          .then(function () {
            // always executed
          });
      })
    },
    tarjetaVencida(fechaTarjeta){
    
      //cut(card.thruDate,5,2) }} / {{cut(card.thruDate,2,2)
      let vencimientoTarjeta = this.cut(fechaTarjeta,5,2)+"/"+this.cut(fechaTarjeta,2,2);
      let fechaActual = new Date();
      
      fechaActual= String(fechaActual.getMonth() + 1).padStart(2, '0') + '/' + credit_cards.cut(fechaActual.getFullYear(),2,2);
      
      if(fechaActual>=vencimientoTarjeta){
        return true
      }
      return false;
    }
  },
  computed: {    
    
  }

});