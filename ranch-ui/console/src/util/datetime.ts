class DateTime {
    public time(time: number): string {
        const date = new Date(time);

        return date.getFullYear() + '-' + this.format(date.getMonth() + 1, 2) + '-' + this.format(date.getDate(), 2)
            + ' ' + this.format(date.getHours(), 2) + ':' + this.format(date.getMinutes(), 2) + ':' + this.format(date.getSeconds(), 2);
    }

    private format(n: number, length: number): string {
        let str: string = '';
        for (let i = 0; i < length; i++) {
            str = (n % 10) + str;
            n = Math.floor(n / 10);
        }

        return str;
    }
}

export default new DateTime();