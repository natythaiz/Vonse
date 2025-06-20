// configuracao do money mask
const moneyMaskOptions = {
  prefix: "",
  suffix: "",
  fixed: true,
  fractionDigits: 2,
  decimalSeparator: ",",
  thousandsSeparator: ".",
  autoCompleteDecimal: true,
};

const Toast = Swal.mixin({
  toast: true,
  position: "center-end",
  iconColor: "white",
  customClass: {
    popup: "colored-toast",
  },
  showConfirmButton: false,
  timer: 3000,
  timerProgressBar: true,
  grow: "row",
  didOpen: (toast) => {
    toast.onmouseenter = Swal.stopTimer;
    toast.onmouseleave = Swal.resumeTimer;
  },
});

atualizarCodigoNaPagina();

function atualizarCodigoNaPagina() {
  prepararComponentesMoney();
  prepararComponentesData();
  prepararComponentesHora();
  prepararConfirmacoes();
  mostrarNotificacaoFromHTML();
}

//Busca campos input com a classe componentemoney na pagina atual e liga-os com
// o componente mask money.
function prepararComponentesMoney() {
  let inputsMoney = document.querySelectorAll(".componentemoney");
  inputsMoney.forEach(function (input) {
    SimpleMaskMoney.setMask(input, moneyMaskOptions);
    input.classList.remove("componentemoney");
  });
}

//Busca campos input com a classe componentedata na pagina atual e liga-os com
// o componente date picker. Os input devem ter u ID
function prepararComponentesData() {
  let inputsData = document.querySelectorAll(".componentedata");
  inputsData.forEach(function (input) {
    input.classList.remove("componentedata");
    var datePicker;
    if (input.value !== "") {
      const dateParts = input.value.split("/");
      const dateObject = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]);
      datePicker = MCDatepicker.create({
        el: String("#" + input.id),
        dateFormat: "dd/mm/yyyy",
        selectedDate: dateObject,
        customWeekDays: ["Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"],
        customMonths: ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"],
        customClearBTN: "Limpar",
        customCancelBTN: "Cancelar",
        theme: {
          theme_color: "rgb(31 41 55)",
        },
      });
    } else {
      datePicker = MCDatepicker.create({
        el: String("#" + input.id),
        dateFormat: "dd/mm/yyyy",
        customWeekDays: ["Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"],
        customMonths: ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"],
        customClearBTN: "Limpar",
        customCancelBTN: "Cancelar",
        theme: {
          theme_color: "rgb(31 41 55)",
        },
      });
    }
    datePicker.onOpen(() => {
      if (input.value !== "" && input.value !== datePicker.getFormatedDate()) {
        const dateParts = input.value.split("/");
        const dateObject = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]);
        datePicker.setFullDate(dateObject);
      }
    });
  });
}

//Busca campos input com a classe componentehora na pagina atual e liga-os com
// o componente time picker.
function prepararComponentesHora() {
  let inputsHora = document.querySelectorAll(".componentehora");
  inputsHora.forEach(function (input) {
    input.classList.remove("componentehora");
    const dialog = new mdDateTimePicker.default({
      type: "time",
      mode: true,
      inner24: true,
      cancel: "Cancelar",
    });
    input.addEventListener("click", function () {
      dialog.toggle();
    });
    dialog.trigger = input;
    input.addEventListener("onOk", function () {
      this.value = dialog.time.format("HH:mm");
    });
  });
}

//Busca por elementos com o atributo hx-confirm e usa o SweetAlert2 para pedir uma
// confirmacao do usuario antes de efetuar a acao.
//O valor do atributo hx-confirm eh exibido na pergunta.
//Caso o usuario aceite, a acao eh submetida, caso cancele ou feche nada acontece.
function prepararConfirmacoes() {
  let elementos = document.querySelectorAll("[hx-confirm]");
  elementos.forEach(function (elemento) {
    elemento.removeEventListener("htmx:confirm", dispararConfirmacao);
    elemento.addEventListener("htmx:confirm", dispararConfirmacao);
  });
}

function dispararConfirmacao(e) {
  e.preventDefault();
  Swal.fire({
    title: "Você tem certeza?",
    text: e.detail.question,
    icon: "warning",
    showCancelButton: true,
    cancelButtonText: "Cancelar",
    confirmButtonText: "Remover",
    confirmButtonColor: "#3085d6",
  }).then((result) => {
    if (result.isConfirmed) {
      e.detail.issueRequest(true); // true to skip the built-in window.confirm()
    }
  });
}

//Busca por um input hidden com o id mensagemSA2 no HTML da página atual.
//Caso encontre, busca por um input hidden com o id tipoSA2 e um input hidden com o id intervaloSA2.
//Mostra uma notificao do SweetAlert2 desse tipo com essa mensagem por esse intervalo.
function mostrarNotificacaoFromHTML() {
  let inputMensagem = document.getElementById("mensagemSA2");
  if (inputMensagem !== null && inputMensagem.value !== "") {
    let mensagem = inputMensagem.value;
    let tipo = document.getElementById("tipoSA2").value;
    let intervalo = document.getElementById("intervaloSA2").value;
    Toast.fire({
      icon: tipo,
      title: mensagem,
      timer: intervalo,
    });
  }
}

// Para aplicar o codigoninjadogodoi no HTML que foi colocado na pagina
// usando htmx
htmx.on("htmx:afterSettle", function (evt) {
  atualizarCodigoNaPagina();
});
