let accounts = new Vue({
  el: '#accounts',
  
  data: {
    cuentas: [],
    loans: [],
    cliente: "",
    mostrarTipoCuenta: false,
    tipoCuenta: ""
  },

  created() {
    this.loadData();
  },

  methods: {

    loadData() {
      axios.get('api/clients/current')
        .then(function (response) {
          console.log(response);
          accounts.cliente = response.data.firstName;
          accounts.cuentas = response.data.accounts;
          accounts.loans = response.data.loans;
          console.log(accounts.loans);
        })
        .catch(function (error) {
          // handle error
          console.log(error);
        })
        .then(function () {
          // always executed
        });
    },
    createAccount() {
      console.log(this.tipoCuenta);
      axios.post('/api/clients/current/accounts', `tipoCuenta=${this.tipoCuenta}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
        .then(response => {
          Swal.fire({
            text: 'Cuenta creada con exito',
            icon: 'success',
            confirmButtonText: 'Ok',
          }).then(() => {
            location.reload();
          })
        })
        .catch(function (error) {
          // handle error
          console.log(error);
          Swal.fire({
            icon: 'error',
            text: 'No se puede crear la cuenta, el usuario ya posee 3 cuentas, que es el máximo permitido!',
            showConfirmButton: false
          });
        })
        .then(function () {
          // always executed
        });
    },
    toUpper(word) {
      return word.toUpperCase();
    },
    capitalizeWord(word) {
      return word.charAt(0).toUpperCase() + word.slice(1)
    },
    showTipoCuenta() {
      this.mostrarTipoCuenta = true;
    },
    valorCuota(valorPrestamo, cuotas) {
      return valorPrestamo / cuotas;
    }
    ,
    cerrarSesion() {
      Swal.fire({
        title: 'Estas seguro/a de cerrar Sesión?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3EBD02',
        cancelButtonColor: '#d33',
        cancelButtonText: 'Cancelar',
        confirmButtonText: 'Si, Quiero cerrar sesión!'
      }).then((result) => {
        if (result.isConfirmed) {
          axios.post('/api/logout').then(response => {
            console.log('signed out!!!');
            window.location.href = "/inicio_sesion.html";
          })
        }
      })
    },
    transacciones() {
      let transacciones = [];
      this.cuentas.forEach(cuenta => {
        //Devuelvo las ultimas 4 transacciones
        cuenta.transactions.forEach(transaction => {
          transacciones.push(transaction);
        });
      });
      console.log(transacciones);
      return transacciones.slice(0, 6);
    },
    getMonth(date) {
      let month = "";
      let monthNum = date.substr(5, 2);

      switch (monthNum) {
        case '01': month = "EN";
          break;
        case '02': month = "FEB";
          break;
        case '03': month = "MAR";
          break;
        case '04': month = "ABR";
          break;
        case '05': month = "MAY";
          break;
        case '06': month = "JUN";
          break;
        case '07': month = "JUL";
          break;
        case '08': month = "AGO";
          break;
        case '09': month = "SEP";
          break;
        case '10': month = "OCT";
          break;
        case '11': month = "SAG";
          break;
        default: month = "DIC";
      }
      console.log(month);
      return month;

    },
    getDay(date) {
      let day = date.substr(8, 2);
      return day;
    },
    getHour(date) {
      let hour = date.substr(11, 5);
      return hour;
    }
  }
});
