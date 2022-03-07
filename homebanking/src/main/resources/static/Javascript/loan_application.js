let loan = new Vue({
    el: '#loan',
    data: {
        loans: [],
        cuotas: [],
        monto: "",
        payments: "",
        prestamoASolicitar: "",
        cuotasASolicitar: "",
        cuentas:[],
        cuentaDestino:"",        
    },
    created() {
        this.getLoans();
        this.loadData();
    },
    methods: {
        getLoans() {
            axios.get('/api/loans')
                .then(response => {
                    console.log(response.data);
                    this.loans = response.data;
                })
        },
        loadData() {
            axios.get('api/clients/current')
                .then(function (response) {
                    console.log(response);
                    loan.cuentas = response.data.accounts;         
                })
                .catch(function (error) {
                    // handle error
                    console.log(error);
                })
                .then(function () {
                    // always executed
                });
        },
        pedirPrestamo(){
            selectLoan = this.loans.filter(loan => loan.name==this.prestamoASolicitar);
            console.log("ID");
            console.log(this.prestamoASolicitar);
            console.log(selectLoan[0].id);
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
                    axios.post('api/loans', {id:selectLoan[0].id, name:`${this.prestamoASolicitar}`, monto:`${this.monto}`, cuotas:`${this.cuotasASolicitar}`,cuentaDestino:`${this.cuentaDestino}` } )
                        .then(response => {
                            Swal.fire({
                                icon: 'success',
                                text: 'Prestamo solicitado exitosamente!',
                                showConfirmButton: true
                            });
                            this.reiniciarValores();
                            this.loadData();

                            
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
        },
        reiniciarValores(){
                  
            this.monto= "";
            this.payments= "";
            this.prestamoASolicitar= "";
            this.cuotasASolicitar= "";           
            this.cuentaDestino="";      
        } 
    },
    computed: {
        getPayments: function () {
            console.log(this.prestamoASolicitar);
            let loan = "";
            if (this.prestamoASolicitar != "") {
                loan = this.loans.filter(loan => loan.name == this.prestamoASolicitar);
                return loan[0].payments;
            }

        },
        getMaxAmount: function () {
            if (this.prestamoASolicitar != "") {
                loan = this.loans.filter(loan => loan.name == this.prestamoASolicitar);
                return loan[0].maxAmount;
            }
        },
        getInterest:function(){
            if (this.prestamoASolicitar != "") {
                loan = this.loans.filter(loan => loan.name == this.prestamoASolicitar);
                return loan[0].interest;
            }
        }
    }

});