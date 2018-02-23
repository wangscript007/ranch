import selector from './selector';

class Note {
    private timeout: number = 0;

    public show(code: number, message: string, hide: number = 0): void {
        let element = selector.find('#note');
        if (!element) {
            alert((code > 0 ? ('[' + code + ']') : '') + message);

            return;
        }

        this.clearTimeout();
        let html: string = '<span class="message">' + message + '</span>';
        if (code > 0)
            html = '<span class="code">' + code + '</span>' + html;
        element.innerHTML = html;
        element.style.display = '';

        if (hide > 0)
            this.timeout = window.setTimeout(() => this.hide(), hide);
    }

    public hide(): void {
        let element = selector.find('#note');
        if (element)
            element.style.display = 'none';
        this.clearTimeout();
    }

    private clearTimeout(): void {
        if (this.timeout <= 0)
            return;

        window.clearTimeout(this.timeout);
        this.timeout = 0;
    }
}

export default new Note();