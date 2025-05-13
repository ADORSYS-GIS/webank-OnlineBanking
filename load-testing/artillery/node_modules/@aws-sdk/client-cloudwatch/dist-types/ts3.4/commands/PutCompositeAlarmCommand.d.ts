import { Command as $Command } from "@smithy/smithy-client";
import { MetadataBearer as __MetadataBearer } from "@smithy/types";
import {
  CloudWatchClientResolvedConfig,
  ServiceInputTypes,
  ServiceOutputTypes,
} from "../CloudWatchClient";
import { PutCompositeAlarmInput } from "../models/models_0";
export { __MetadataBearer };
export { $Command };
export interface PutCompositeAlarmCommandInput extends PutCompositeAlarmInput {}
export interface PutCompositeAlarmCommandOutput extends __MetadataBearer {}
declare const PutCompositeAlarmCommand_base: {
  new (
    input: PutCompositeAlarmCommandInput
  ): import("@smithy/smithy-client").CommandImpl<
    PutCompositeAlarmCommandInput,
    PutCompositeAlarmCommandOutput,
    CloudWatchClientResolvedConfig,
    ServiceInputTypes,
    ServiceOutputTypes
  >;
  new (
    input: PutCompositeAlarmCommandInput
  ): import("@smithy/smithy-client").CommandImpl<
    PutCompositeAlarmCommandInput,
    PutCompositeAlarmCommandOutput,
    CloudWatchClientResolvedConfig,
    ServiceInputTypes,
    ServiceOutputTypes
  >;
  getEndpointParameterInstructions(): import("@smithy/middleware-endpoint").EndpointParameterInstructions;
};
export declare class PutCompositeAlarmCommand extends PutCompositeAlarmCommand_base {
  protected static __types: {
    api: {
      input: PutCompositeAlarmInput;
      output: {};
    };
    sdk: {
      input: PutCompositeAlarmCommandInput;
      output: PutCompositeAlarmCommandOutput;
    };
  };
}
