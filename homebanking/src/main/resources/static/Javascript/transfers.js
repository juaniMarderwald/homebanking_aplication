let transfers = new Vue({
    el: '#transfers',
    data: {
        montoTransferencia: "",
        cuentasPropias: [],
        numeroCuentaTercer: "",
        tipoTransferencia: "",
        cuentaDestino: "",
        cuentaOrigen: "",
        descripcionTransferencia: "",
        destinatario:""
        
    },

    created() {
        this.loadData();
        
    },

    methods: {
        loadData() {
            axios.get('api/clients/current')
                .then(function (response) {
                    console.log(response);
                    transfers.cuentasPropias = response.data.accounts;
                    console.log("CUENTAS");
                    console.log(response.data.accounts);
                    
                })
                .catch(function (error) {
                    // handle error
                    console.log(error);
                })
                .then(function () {
                    // always executed
                });
        },

        realizarTransaccion() {

            if (this.cuentaOrigen == "" || this.cuentaDestino == "" || this.montoTransferencia == 0) {

                Swal.fire({
                    icon: 'error',
                    text: 'Faltan algunos datos para poder realizar la transacción!',
                    showConfirmButton: false
                });

            }
            else {

                Swal.fire({
                    title: 'Estas seguro/a?',
                    text: "No se podrá volver atrás!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3EBD02',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Si, realizar transacción!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        axios.post('api/transaction', `originAccountNumber=${transfers.cuentaOrigen}&destinyAccountNumber=${transfers.cuentaDestino}&strAmount=${transfers.montoTransferencia}&description=${transfers.descripcionTransferencia}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                            .then(response => {
                                Swal.fire({
                                    icon: 'success',
                                    text: 'Transaccion realizada con exito!',
                                    showConfirmButton: true
                                });
                                this.reiniciarValores();
                            })
                            .catch(error => {
                                Swal.fire({
                                    icon: 'error',
                                    text: error.response.data,
                                    showConfirmButton: true
                                });
                                this.reiniciarValores();
                            })

                    }
                })
            }

        },
        reiniciarValores() {
            transfers.montoTransferencia = "";
            transfers.cuentaOrigen = "";
            transfers.cuentaDestino = "";
            transfers.descripcionTransferencia = "";
        }
    },
    computed:{
        nombreDestinatario: function (){
            nombreCuentaDestino ="";
            axios.get(`api/account/${transfers.cuentaDestino}`)
            .then(response=>{
                console.log(response.data);
                nombreCuentaDestino=response.data;
                this.destinatario=response.data;
            })
            .catch(function (error) {
                // handle error
                console.log(error);
            })
            .then(function () {
                // always executed
            });
            console.log("llego aca");
            console.log(this.destinatario);
            console.log("mostro");
            return this.destinatario;
        },
        ultimosMovimientos: function(){
           let movimientosCuenta = [];
           this.cuentasPropias.forEach(element => {
                element.transactions.forEach(transaction => {
                    movimientosCuenta.push(transaction);                    
                });                    
           });
           console.log("MOVIMIENTOS CUENTA");
           console.log(movimientosCuenta);
           return movimientosCuenta; 
        }
    }
});