function formatPhone(input) {
    let value = input.value;

    if (event.inputType === 'deleteContentBackward' || event.inputType === 'deleteContentForward') {
        return;
    }

    let digits = value.replace(/\D/g, '');

    if (digits.length === 0) {
        input.value = '+7 ';
        return;
    }

    if (!digits.startsWith('7')) {
        digits = '7' + digits;
    }

    let formattedNumber = '+7 ' + digits.slice(1, 4);
    if (digits.length > 3) formattedNumber += ' ' + digits.slice(4, 7);
    if (digits.length > 6) formattedNumber += '-' + digits.slice(7, 9);
    if (digits.length > 8) formattedNumber += '-' + digits.slice(9, 11);

    input.value = formattedNumber.slice(0, 16);
}
