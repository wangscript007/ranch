import * as React from 'react';
import Radio from '../../../ui/radio';
import Check from '../../../ui/check';

export default class Dashboard extends React.Component {
    render() {
        return (
            <div>
            <Radio name="radio" list={[
                {
                    value:"1",
                    label:"radio 1"
                },
                {
                    value:"2",
                    label:"radio 2"
                },
                {
                    value:"3",
                    label:"radio 3"
                }
            ]}/>
            <Check name="check" list={[
                {
                    value:"1",
                    label:"check 1"
                },
                {
                    value:"2",
                    label:"check 2"
                },
                {
                    value:"3",
                    label:"check 3"
                }
            ]}/>
            </div>
        );
    }
}