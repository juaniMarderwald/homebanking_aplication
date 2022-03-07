let sesion = new Vue({
    el: '#sesion',
    data: {
        name: "",
        password: "",
    },

    created() { this.name="";
                this.password=""; },

    methods: {
        iniciarSesion() {

            console.log(this.name);
            console.log(this.password);

            if(this.name== "" || this.password==""){
                console.log("ENTRA");

                Swal.fire({
                    icon: 'error',
                    text: 'Faltan Completar campos !',
                    showConfirmButton: true
                });
            }
            else{
                console.log("ENTRA");
                axios.post('/api/login', "email=" + this.name + "&" + "password=" + this.password, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    console.log('signed in!!!');
                    if (this.name == "admin") {
                        window.location.href = "/manager.html";
                    }
                    else {
                        window.location.href = "/accounts.html";
                    }

                })
                .catch(function (error) {
                    if (error.response) {
                      // The request was made and the server responded with a status code
                      // that falls out of the range of 2xx
                      // Con el catch agarro el error del response, eso quiere decir que el email o la contraseña son invalidas, o no encontró el usuario o la contraseña está mal, y lo muestro por pantalla
                      Swal.fire({
                        icon: 'error',
                        text: 'Email o contraseña invalido, vuelva a intentar!',
                        showConfirmButton: false
                      });
                      console.log(error.response.data);
                      console.log(error.response.status);
                      console.log(error.response.headers);
                    } else if (error.request) {
                      // The request was made but no response was received
                      // `error.request` is an instance of XMLHttpRequest in the browser and an instance of                                          
                      console.log(error.request);
                    } else {
                      // Something happened in setting up the request that triggered an Error
                      console.log('Error', error.message);
                    }
                    console.log(error.config);
                  });

            }          

        },
        cerrarSesion() {
            Swal.fire({
                title: 'Estas seguro/a de cerrar Sesión?',                
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3EBD02',
                cancelButtonColor: '#d33',
                cancelButtonText:'Cancelar',
                confirmButtonText: 'Si, Quiero cerrar sesión!'
            }).then((result) => {
                if (result.isConfirmed) {                                     
                    axios.post('/api/logout').then(response => {
                        console.log('signed out!!!');
                        window.location.href = "/inicio_sesion.html";
                    })
                }
            })          
        }
    }


});




